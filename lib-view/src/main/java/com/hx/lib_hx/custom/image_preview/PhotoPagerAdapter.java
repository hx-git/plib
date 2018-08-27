package com.hx.lib_hx.custom.image_preview;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.hx.lib_hx.R;
import java.io.File;
import java.util.List;

/**

 */
public class PhotoPagerAdapter extends PagerAdapter {

  private List<String> paths;
  private RequestManager mGlide;

  public PhotoPagerAdapter(RequestManager glide, List<String> paths) {
    this.paths = paths;
    this.mGlide = glide;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    final Context context = container.getContext();
    View itemView = LayoutInflater.from(context)
        .inflate(R.layout.item_pager, container, false);

    final ImageView imageView =  itemView.findViewById(R.id.iv_pager);

    final String path = paths.get(position);
    final Uri uri;
    if (path.startsWith("http")) {
      uri = Uri.parse(path);
    } else {
      uri = Uri.fromFile(new File(path));
    }

    boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);

    if (canLoadImage) {
      final RequestOptions options = new RequestOptions();
      options.dontAnimate()
          .dontTransform()
          .override(800, 800)
          .placeholder(R.drawable.__picker_ic_photo_black_48dp)
          .error(R.drawable.__picker_ic_broken_image_black_48dp);
      mGlide.setDefaultRequestOptions(options).load(uri)
              .thumbnail(0.1f)
              .into(imageView);
    }

    imageView.setOnClickListener(view -> {
      if (context instanceof Activity) {
        if (!((Activity) context).isFinishing()) {
          ((Activity) context).onBackPressed();
        }
      }
    });

    container.addView(itemView);

    return itemView;
  }


  @Override public int getCount() {
    return paths.size();
  }


  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
    mGlide.clear((View) object);
  }

  @Override
  public int getItemPosition (Object object) { return POSITION_NONE; }

}
