package com.igeek.hfrecyleviewlib;

public interface IPullRefreshView {
  /**
   * 隐藏
   */
  void onPullHided();

  /**
   * 刷新中
   */
  void onPullRefreshing();

  /**
   * 提示松手
   */
  void onPullToRefresh();

  /**
   * 刷新过程中
   */
  void releaseToRefresh();

  /**
   * 刷新完成
   */
  void onPullRefreshFinished();

  /**
   * @param pullDistance 下拉的距离
   * @param pullProgress 下拉的距离的比例
   */
  void pullProgress(float pullDistance, float pullProgress);
}
