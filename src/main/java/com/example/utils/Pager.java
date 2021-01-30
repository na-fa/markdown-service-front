package com.example.utils;

import java.util.List;
import java.util.Map;

public class Pager {
  // 現在ページ
  private int current;

  // 総ページ数
  private int totalPages;

  // ナビゲーションページ数、中間ページが前後にリンクを持つため奇数で指定
  private int navigatePages;

  // ナビゲーション中間ページ数
  private int navigateMidPages;

  // 中間ページの最初のページ
  private int navigateMidFirstPage;

  // 中間ページの最後のページ
  private int navigateMidLastPage;

  // ナビゲーションの前方を「...」で丸めるかどうか
  private boolean isRoundFrontPage = false;

  // ナビゲーションの後方を「...」で丸めるかどうか
  private boolean isRoundRearPage = false;

  // 最初のページかどうか
  private boolean isFirstPage = false;

  // 最後のページかどうか
  private boolean isLastPage = false;

  public Pager(
    int currentNumber,
    int totalPages,
    boolean isFirstPage,
    boolean isLastPage
    ) {
    this.current = currentNumber + 1;
    this.totalPages = totalPages;
    this.isFirstPage = isFirstPage;
    this.isLastPage = isLastPage;
    this.navigatePages = 5;
    // 中間ページは最初の1ページと最後のページを抜かした数
    this.navigateMidPages = navigatePages - 2;

    createMidNavigatePage();
  }

  // 中間ページの作成
  private void createMidNavigatePage() {
    // 総ページ数が2以下の場合は中間ページは表示させない
    if (totalPages <= 2) {
      this.navigateMidFirstPage = 0;
      this.navigateMidLastPage = 0;
      this.isRoundFrontPage = false;
      this.isRoundRearPage = false;
    }
    // 総ページ数がナビゲーションページ数以下の場合、ナビゲーションを丸めずに表示
    else if (totalPages <= navigatePages) {
      // 2~(総ページ数 - 1)
      this.navigateMidFirstPage = 2;
      this.navigateMidLastPage = totalPages - 1;
      this.isRoundFrontPage = false;
      this.isRoundRearPage = false;
    }
    // 総ページ数がナビゲーションページ数より多い場合、ナビゲーションを丸める
    else {
      // 現在のページが(最初の1ページ + 中間ページ)以内の場合、後方を丸める
      if (current < 1 + navigateMidPages) {
        // 2~(最初の1ページ + 中間ページ)
        this.navigateMidFirstPage = 2;
        this.navigateMidLastPage = 1 + navigateMidPages;
        this.isRoundFrontPage = false;
        this.isRoundRearPage = true;
      }
      // 現在のページが(総ページ数 - 中間ページ)以降の場合、前方を丸める
      else if (current > totalPages - navigateMidPages) {
        // (総ページ数 - 中間ページ)~(総ページ数 - 1)
        this.navigateMidFirstPage = totalPages - navigateMidPages;
        this.navigateMidLastPage = totalPages - 1;
        this.isRoundFrontPage = true;
        this.isRoundRearPage = false;
      }
      // 前方と後方を丸める
      else {
        this.navigateMidFirstPage = current - navigateMidPages / 2;
        this.navigateMidLastPage = current + navigateMidPages / 2;
        this.isRoundFrontPage = true;
        this.isRoundRearPage = true;
      }
    }

  }

  public int getCurrent() {
    return current;
  }

  public void setCurrent(int current) {
    this.current = current;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public int getNavigatePages() {
    return navigatePages;
  }

  public void setNavigatePages(int navigatePages) {
    this.navigatePages = navigatePages;
  }

  public int getNavigateMidPages() {
    return navigateMidPages;
  }

  public void setNavigateMidPages(int navigateMidPages) {
    this.navigateMidPages = navigateMidPages;
  }

  public int getNavigateMidFirstPage() {
    return navigateMidFirstPage;
  }

  public void setNavigateMidFirstPage(int navigateMidFirstPage) {
    this.navigateMidFirstPage = navigateMidFirstPage;
  }

  public int getNavigateMidLastPage() {
    return navigateMidLastPage;
  }

  public void setNavigateMidLastPage(int navigateMidLastPage) {
    this.navigateMidLastPage = navigateMidLastPage;
  }

  public boolean getIsRoundFrontPage() {
    return isRoundFrontPage;
  }

  public void setIsRoundFrontPage(boolean isRoundFrontPage) {
    this.isRoundFrontPage = isRoundFrontPage;
  }

  public boolean getIsRoundRearPage() {
    return isRoundRearPage;
  }

  public void setIsRoundRearPage(boolean isRoundRearPage) {
    this.isRoundRearPage = isRoundRearPage;
  }

  public boolean getIsFirstPage() {
    return isFirstPage;
  }

  public void setIsFirstPage(boolean isFirstPage) {
    this.isFirstPage = isFirstPage;
  }

  public boolean getIsLastPage() {
    return isLastPage;
  }

  public void setIsLastPage(boolean isLastPage) {
    this.isLastPage = isLastPage;
  }

  @Override
  public String toString() {
    return "Pager{" +
      "current='" + current + '\'' +
      ", totalPages='" + totalPages + '\'' +
      ", navigatePages='" + navigatePages + '\'' +
      ", navigateMidPages='" + navigateMidPages + '\'' +
      ", navigateMidFirstPage='" + navigateMidFirstPage + '\'' +
      ", navigateMidLastPage='" + navigateMidLastPage + '\'' +
      ", isRoundFrontPage='" + isRoundFrontPage + '\'' +
      ", isRoundRearPage='" + isRoundRearPage + '\'' +
      ", isFirstPage='" + isFirstPage + '\'' +
      ", isLastPage='" + isLastPage + '\'' +
    '}';
  }
}