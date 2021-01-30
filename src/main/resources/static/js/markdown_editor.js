$(function () {
  // ツールバーの設定
  const toolbar = [
    'heading-smaller',
    'bold',
    'strikethrough',
    'italic',
    'heading',
    'code',
    '|',
    'quote',
    'unordered-list',
    'ordered-list',
    '|',
    'link',
    'upload-image',
    'table',
    '|',
    'preview',
    'side-by-side',
    'fullscreen',
    '|',
    {
      name: "guide",
      action: "/",
      className: "fa fa-question-circle",
      title: "guide"
    },
  ];

  // 画像のアップロード
  const imageUploadFunction = (file, onSuccess, onError) => {
    var formData = new FormData();
    var imageUrl;
    formData.append('file', file);
    $.ajax({
      type: 'POST',
      url: '/upload/article/image',
      processData: false,
      contentType: false,
      data: formData
    }).done(function(data, textStatus, jqXHR){
      imageUrl = data;
      onSuccess(imageUrl)
    }).fail(function(jqXHR, textStatus, errorThrown){
      onError('upload failed.');
    });
  };

  var easyMDE = new EasyMDE({
    element: document.getElementById('editor'),
    hideIcons: ['side-by-side', 'fullscreen'],
    toolbar: toolbar,
    previewClass: ["editor-preview", "markdown-body"],
    minHeight: '800px',
    sideBySideFullscreen: false,
    uploadImage: true,
    imageAccept: 'image/png, image/jpeg, image/gif',
    imageUploadFunction: imageUploadFunction,
    errorCallback: errorMessage => {
      console.error(errorMessage);
    }
  });
});