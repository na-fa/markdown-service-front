$(function () {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });

  $(document).ready(function() {
    $('.drawer').drawer();
  });

  // dropDown userMenu
  $("#header-usermenu").hover(function(){
    $("#header-usermenu-link").stop().slideDown("fast");
  }, function(){
    $("#header-usermenu-link").stop().slideUp("fast");
  });

  // 一覧からのブックマーク
  $(".article-list .bookmark").on("click", function() {
    var articleHash = $(this).parent().data('hash');
    var userName = $(this).parent().data('name');

    $.ajax({
      type: 'POST',
      url: '/api/bookmark',
      data: {'userName' : userName, 'articleHash' : articleHash}
    }).done(function(data, textStatus, jqXHR){
      var dispOn = $('[data-hash="' + articleHash + '"]').find(".bookmark.disp-on");
      var dispOff = $('[data-hash="' + articleHash + '"]').find(".bookmark.disp-off");

      // 非表示から表示
      dispOff.removeClass("disp-off");
      dispOff.addClass("disp-on");
    
      // 表示から非表示
      dispOn.removeClass("disp-on");
      dispOn.addClass("disp-off");
    }).fail(function(jqXHR, textStatus, errorThrown){
      alert('Bookmark Failed.');
    });
  });

  // 詳細からのブックマーク
  $(".detail .bookmark").on("click", function() {
    const pathArray = location.pathname.substring(1).split('/');
    const userName = pathArray[0];
    const articleHash = pathArray[pathArray.length - 1];

    $.ajax({
      type: 'POST',
      url: '/api/bookmark',
      data: {'userName' : userName, 'articleHash' : articleHash}
    }).done(function(data, textStatus, jqXHR){
      var dispOn = $(".bookmark.disp-on");
      var dispOff = $(".bookmark.disp-off");

      // 非表示から表示
      dispOff.removeClass("disp-off");
      dispOff.addClass("disp-on");

      // 表示から非表示
      dispOn.removeClass("disp-on");
      dispOn.addClass("disp-off");
    }).fail(function(jqXHR, textStatus, errorThrown){
      alert('Bookmark Failed.');
    });
  });

  // タグ選択
  $('#tag-select').select2({
    placeholder: "タグ",
    width: '100%',
    ajax: { 
      type: 'POST',
      url: "/draft/tags",
      dataType: "json",
      delay: 250,
      data: function (params) {
        var queryParameters = {
          q: params.term
        }
        return queryParameters;
      },
      processResults: function (data) {
        var results = [];
        data.forEach(function(val){
          results.push({id: val['id'], text: val['name']});
        });
        return {
          results: results
        };
      }
    },
  });

  // 記事投稿
  $('.article-save__button').on('click', function() {
    let form = $('#article-form');
    form.attr('action', location.pathname);
    let status = $(this).data('status');
    $('<input>').attr({
      'type': 'hidden',
      'name': 'status',
      'value': status
    }).appendTo(form);
    form.submit();
  });

  // 下書き一覧から記事を削除
  $('.draft-delete-button').on('click', function() {
    var articleHash = $(this).data('hash');
    $.ajax({
      type: 'POST',
      url: '/draft/list',
      data: {'articleHash' : articleHash}
    }).done(function(data, textStatus, jqXHR){
      location.reload();
    }).fail(function(jqXHR, textStatus, errorThrown){
      alert('Delete Failed.');
    });
  });

  // logout
  $('#logout-btn').on('click', function() {
    var form = $(this).parents('form');
    form.attr('action', $(this).data('action'));
    form.submit();
  });
});