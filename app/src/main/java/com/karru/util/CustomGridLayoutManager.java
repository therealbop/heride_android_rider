package com.karru.util;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;

public class CustomGridLayoutManager extends GridLayoutManager {
 private boolean isScrollEnabled = true;


 public CustomGridLayoutManager(Context context, int spanCount, boolean isScrollEnabled) {
  super(context, spanCount);
  this.isScrollEnabled = isScrollEnabled;
 }

 public void setScrollEnabled(boolean flag) {
  this.isScrollEnabled = flag;
 }

 @Override
 public boolean canScrollVertically() {
  //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
  return isScrollEnabled && super.canScrollVertically();
 }
}