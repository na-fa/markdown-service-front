package com.example.controller;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.model.ArticleImage;
import com.example.service.UploadService;
import com.example.service.impl.UserDetailsImpl;
import com.example.properties.WebProperties;

@Controller
@RequestMapping("/upload")
public class UploadController {
  private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

  private final UploadService uploadService;
  private final WebProperties webProperties;

  public UploadController(UploadService uploadService, WebProperties webProperties) {
    this.uploadService = uploadService;
    this.webProperties = webProperties;
  }

  /**
   * 画像の拡張子が正しいかを確認
   * @param file 画像ファイル
   * @return ext 拡張子
   */
  private String imageFileExtCheck(MultipartFile file) {
    // 拡張子の取得
    String originalFileName = file.getOriginalFilename();
    String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

    // メディアタイプの取得
    MediaType mediaType = MediaType.parseMediaType(file.getContentType());

    List<MediaType> mediaTypeList = Arrays.asList(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG, MediaType.IMAGE_GIF);
    List<String> extList = Arrays.asList("jpg", "jpeg", "png", "gif");

    boolean mediaTypeExist = mediaTypeList.contains(mediaType);
    boolean extExists = extList.contains(ext);
    if (!mediaTypeExist || !extExists) {
      return null;
    }
    return ext;
  }

  // 画像のアップロードを行いmarkdownの文字列を返す
  @PostMapping("/article/image")
  @ResponseBody
  public ResponseEntity<String> imagePost(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      @RequestParam("file") MultipartFile file) {
    if (userDetailsImpl == null) {
      return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
    }

    String ext = imageFileExtCheck(file);
    if (ext == null) {
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    // uuidを生成し保存するファイル名を取得
    UUID randomUUID = UUID.randomUUID();
    String uuid = randomUUID.toString();
    String fileName = uuid + "." + ext;

    // アップロードするパスを今日の日付から作成
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
    String path = dateFormat.format(date);

    // ファイルのアップロード場所
    StringBuilder filePathTmp = new StringBuilder(webProperties.getUploadPath());
    filePathTmp.append(path + '/');
    String fileDirPath = filePathTmp.toString();
    filePathTmp.append(fileName);
    String filePath = filePathTmp.toString();

    // アップロード後のURL
    StringBuilder imageURLTmp = new StringBuilder(webProperties.getCdnUrl());
    imageURLTmp.append('/' + path + '/');
    imageURLTmp.append(fileName);
    String imageURL = imageURLTmp.toString();

    // アップロードするディレクトリがなければ作成
    File targetDir = new File(fileDirPath);
    System.out.println(targetDir);
    if(!targetDir.exists()){
      targetDir.mkdirs();
    }

    File uploadFile = new File(filePath);
    try {
      // 画像情報をDBに保存
      ArticleImage articleImage = new ArticleImage();
      articleImage.setUser(userDetailsImpl.getUser());
      articleImage.setPath(path);
      articleImage.setUuid(uuid);
      articleImage.setExtension(ext);
      uploadService.saveImage(articleImage);

      // ファイルをアップロードする
      byte[] bytes = file.getBytes();
      BufferedOutputStream uploadFileStream = new BufferedOutputStream(new FileOutputStream(uploadFile));
      uploadFileStream.write(bytes);
      uploadFileStream.close();
    } catch (IOException e) {
      logger.error(e.getMessage());
      return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    return new ResponseEntity<String>(imageURL, HttpStatus.OK);
  }
}