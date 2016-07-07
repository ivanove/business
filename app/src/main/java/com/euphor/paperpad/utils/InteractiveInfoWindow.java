package com.euphor.paperpad.utils;

import android.R.style;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.euphor.paperpad.R;
import com.euphor.paperpad.activities.fragments.WebViewFragment;
import com.euphor.paperpad.activities.main.MainActivity;
import com.euphor.paperpad.Beans.Illustration;
import com.euphor.paperpad.Beans.interactiveMap.Point;
import com.euphor.paperpad.utils.actionsPrices.QuickAction;
import com.euphor.paperpad.utils.quickAction.PopupWindows;
import com.euphor.paperpad.widgets.ArrowImageView;
import com.euphor.paperpad.widgets.BubbleDrawable;


import java.io.File;

/**
 * 
 *  EuphorDev04
 * 
 */

public class InteractiveInfoWindow extends PopupWindows implements OnDismissListener{

	private View mView, v;
	private View mRootView;
	private Context context;
	private LayoutInflater mInflater;
	private Point point;
	private QuickAction.OnDismissListener mDismissListener;

	//private int mAnimStyle;

	private Colors colors;
	//private String groupColor;


	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;

	private static String POLICE, POLICE_DISCRIPT;
	private int width, height;

	private BubbleDrawable myBubble;
	//
	//	public MyLocationInfos(Context context){
	//		super(context);
	//	}
	//private View arrow;


	public InteractiveInfoWindow(Context context, Point point, Colors colors) {
		super(context);

		this.context = context;
		this.point = point;
		this.colors = colors;
		
		//		mOrientation = orientation;

		mInflater 	 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//if (mOrientation == HORIZONTAL) {
		setRootViewId(R.layout.calendar_info_window);
		//        } else {
		//            setRootViewId(R.layout.info_window);
		//        }

		POLICE = "fonts/gill-sans-light.ttf";
		POLICE_DISCRIPT = "fonts/gill-sans-mt-italic.ttf";


	}


	/**
	 * Set root view.
	 * 
	 * @param id Layout resource id
	 */
	public void setRootViewId(int id) {

		mRootView	= (ViewGroup) mInflater.inflate(id, null, false);
		mView = mRootView;
		//mScroller	= (ScrollView) mRootView.findViewById(R.id.scroller);

		//This was previously defined on show() method, moved here to prevent force close that occured
		//when tapping fastly on a view to show quickaction dialog.
		//Thanx to zammbi (github.com/zammbi)
		mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		setContentView(mRootView);
	}


	public void setViewByGridLocation(int BubbleDrection){


		if(v != null)
			v.setSelected(false);
		
		Typeface font, font_discript;
		
		if(MainActivity.FONT_TITLE.getStyle() != Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf").getStyle()){
			font = MainActivity.FONT_TITLE;
			font_discript = MainActivity.FONT_BODY;
		}else{
			font = Typeface.createFromAsset(context.getAssets(), POLICE);
			font_discript = Typeface.createFromAsset(context.getAssets(), POLICE_DISCRIPT);
		}

		//LayoutInflater inflater = LayoutInflater.from(context);
		//mView = mInflater.inflate(R.layout.info_window, null, false);
//		arrow = new View(context);
//		arrow.setLayoutParams(new LayoutParams(200, 100));
		LinearLayout backHolder = (LinearLayout)mView.findViewById(R.id.backHolder);
		//Drawable popover = null;// = context.getResources().getDrawable(R.drawable.popover);
       // BubbleDrawable
        String color = point.getColor();
        if(color == null || color.isEmpty()){
            color = "000000";
        }

        myBubble = new BubbleDrawable(BubbleDrawable.RIGHT, BubbleDrawable.CENTER, colors.getColor(color), colors.getColor(colors.getBackground_color()));
        //myBubble.setColors(Color.BLUE, Color.WHITE);
        myBubble.setCornerRadius(8);
        myBubble.setPointerAlignment(BubbleDrawable.RIGHT, BubbleDrawable.CENTER);
        myBubble.setPadding(8, 15, 0, 4);
		switch (BubbleDrection) {
		case 0:
			//popover = context.getResources().getDrawable(R.drawable.right_bubble_);
	        myBubble.setPointerAlignment(BubbleDrawable.RIGHT, BubbleDrawable.CENTER);
			break;

		case 90:
			//popover = context.getResources().getDrawable(R.drawable.top_bubble_);
	        myBubble.setPointerAlignment(BubbleDrawable.CENTER, BubbleDrawable.TOP);
			break;

		case 180:
			//popover = context.getResources().getDrawable(R.drawable.left_bubble_);
	        myBubble.setPointerAlignment(BubbleDrawable.LEFT, BubbleDrawable.CENTER);
			break;

		case 270:
			//popover = context.getResources().getDrawable(R.drawable.bottom_bubble_);
	        myBubble.setPointerAlignment(BubbleDrawable.CENTER, BubbleDrawable.BOTTOM);
			break;

		default:
			//popover = context.getResources().getDrawable(R.drawable.left_bubble_);
	        myBubble.setPointerAlignment(BubbleDrawable.LEFT, BubbleDrawable.CENTER);
			break;
		}

		mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

	        @Override
	        public void onGlobalLayout() {
	            // Ensure you call it only once :
	        	mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	        	int h = mRootView.findViewById(R.id.dateBubbleTV).getHeight();
	    		myBubble.setMarge(h + 10);//.setPadding(8, 15, 0, 4);
	    		myBubble.invalidateSelf();
	            // Here you can get the size :)
	        }
	    });
		//popover.setColorFilter(new PorterDuffColorFilter(colors.getColor(colors.getBackground_color()), PorterDuff.Mode.MULTIPLY));
		//backHolder.setBackgroundDrawable(popover);
		backHolder.setBackgroundDrawable(myBubble);
		//arrow.setBackgroundDrawable(popover);

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		//Display display = wm.getDefaultDisplay();
		//		mLocationInfos.dismiss();
		DisplayMetrics metrics = new DisplayMetrics(); 
		//int orientation = display.getOrientation();
		//((Activity)context).getWindowManager()
		 //myBubble.setPadding(8, 60, 0, 4);

		wm.getDefaultDisplay().getMetrics(metrics); 
		if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
			width = 410;
			height = 420;
		}else if(metrics.densityDpi == 240) {
			width = 390;
			height = 400;
		}
//		else if(metrics.densityDpi == 320){
//			width = 600;
//			height = 850;
//			 //myBubble.setPadding(8, 60, 0, 4);
//		}
		else if(metrics.densityDpi == 320){
			width = 650;
			height = 650;
		}
		else if(metrics.densityDpi > 219 && metrics.densityDpi < 480) {
			width = 540;
			height = 600;
		}else if(metrics.densityDpi >= 480) {
			width = 500;
			height = 650;
		}else {
			width = 360;
			height = 400;
		}

		
		//text = (c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())+" "+day+" "+DateFormatSymbols.getInstance(Locale.getDefault()).getMonths()[month]+" "+year);


		
		TextView infoWinDate = (TextView)mView.findViewById(R.id.dateBubbleTV);
/*		infoWinDate.setTextAppearance(context, style.TextAppearance_Large);
		infoWinDate.setTypeface(font);
		infoWinDate.setText("");
		infoWinDate.setTextColor(colors.getColor(colors.getBackground_color()));*/
        infoWinDate.setVisibility(View.GONE);
		//infoWinDate.setBackgroundColor(colors.getColor(groupColor,"CC"));

		TextView infoWinTV = (TextView)mView.findViewById(R.id.mapBubbleTV);
		infoWinTV.setText(point.getTitle());
		infoWinTV.setTypeface(font, Typeface.BOLD);
		infoWinTV.setTextColor(colors.getColor(color));

		//height += infoWinTV.getMeasuredHeight();

//		ImageView closeInfoWindow = (ImageView)mView.findViewById(R.id.closeInfoWindow);
//
//		//		Glide.with(activity).load(new File("android_asset/icon/group_38/icon_0_38.png")).into(closeInfoWindow);
//
//		closeInfoWindow.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mWindow.dismiss();
//			}
//		});
		//String imageUrl = event.getImage();
		ImageView infoWinImg = (ImageView)mView.findViewById(R.id.mapBubbleImg);
		infoWinImg.setScaleType(ScaleType.CENTER_CROP);
		Illustration illustration = point.getIllustration();

		if (illustration!=null) {
			infoWinImg.setVisibility(View.VISIBLE);
			//height +=  (int) context.getResources().getDimension(R.dimen.infoWindow_height);

			//			if (!illustration.getPath().isEmpty()) {
			//				imageUrl = "file:///" + illustration.getPath();
			//			}
			//infoWinImg.setImageURI(Uri.parse(imageUrl));
			String path = "";
            if(illustration != null) {
                if (!illustration.getPath().isEmpty()) {
                    path = illustration.getPath();
                    Glide.with(context).load(new File(path)).into(infoWinImg);
                } else if (illustration.getLink() != null && !illustration.getLink().isEmpty()) {
                    path = illustration.getLink();
                    Glide.with(context).load((path.isEmpty()) ? "http" : path).into(infoWinImg);
                }else {
                    infoWinImg.setVisibility(View.GONE);
                    height -=  (int) context.getResources().getDimension(R.dimen.infoWindow_height);
                }
            }else {
				infoWinImg.setVisibility(View.GONE);
				height -=  (int) context.getResources().getDimension(R.dimen.infoWindow_height);
			}
			//			height += infoWinImg.getMeasuredHeight();
		}else {
			infoWinImg.setVisibility(View.GONE);
			height -=  (int) context.getResources().getDimension(R.dimen.infoWindow_height);	
		}


		TextView txt_hour = (TextView)mView.findViewById(R.id.hourBubble);


			txt_hour.setVisibility(View.GONE);
			//height -= 20;//txt_hour.getMeasuredHeight();


		TextView txt_descript = (TextView)mView.findViewById(R.id.mapBubbleDiscript);

		if(point.getText() != null && !point.getText().isEmpty()) {
			//			height += 100;
			txt_descript.setVisibility(View.VISIBLE);
			txt_descript.setText(point.getText());
            txt_descript.setTextAppearance(context, style.TextAppearance_Medium);
			txt_descript.setTypeface(font_discript);
			txt_descript.setTextColor(colors.getColor(color));

			height += 40;//txt_descript.getMeasuredHeight();
		}
		else {
			txt_descript.setVisibility(View.GONE);
			//height -= 20;
		}
		
		LinearLayout buttonHolder = (LinearLayout)mView.findViewById(R.id.buttonHolder);
		
		if (point.getLink()!=null && !point.getLink().isEmpty()) {
			
			height += 20; // += buttonHolder.getMeasuredHeight(); //		
			
			TextView btnTV = (TextView)mView.findViewById(R.id.tvButton);
			btnTV.setTextAppearance(context, style.TextAppearance_Medium);
			btnTV.setTypeface(MainActivity.FONT_BODY);
			btnTV.setTextColor(colors.getColor(color));
			btnTV.setText(context.getString(R.string.popup_website));
			
			ArrowImageView arrowIW = (ArrowImageView)mView.findViewById(R.id.arrowInfoWindow);
			buttonHolder.setVisibility(View.VISIBLE);

			Paint paint = new Paint();
			paint.setColor(colors.getColor(color,"AA"));
			arrowIW.setPaint(paint);
			buttonHolder.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {

					String link = point.getLink();

				WebViewFragment webViewFragment = new WebViewFragment();

				((MainActivity)context).bodyFragment = "WebViewFragment";

				// In case this activity was started with special instructions from an Intent,

				// pass the Intent's extras to the fragment as arguments

				((MainActivity)context).extras = new Bundle();

				if (link.contains(".pdf")) {

						link = "http://docs.google.com/gview?embedded=true&url=" + link;

					}

				((MainActivity)context).extras.putString("link", link);

				webViewFragment.setArguments(((MainActivity)context).extras);

				// Add the fragment to the 'fragment_container' FrameLayout

				((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, webViewFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

		dismiss();
				}
			});		
		}else {

			buttonHolder.setVisibility(View.GONE);
			height -= 30;

        }

		
		mRootView.setLayoutParams(new LayoutParams(width, height));
		mView.setLayoutParams(new LayoutParams(width, height));

	}

	private boolean showItineraryButton;

	public void setItineraryButton(boolean showItineraryButton) {

		this.showItineraryButton = showItineraryButton;

	}
	public boolean showItineraryButton() {

		return this.showItineraryButton;
	}

	public void hide(){
		mWindow.dismiss();
	}

	public void show (View anchor, float x, float y, int zone, int screenHeight) {

		preShow();
		mWindow.setWidth(width);
		mWindow.setHeight(height);
		v = new View(context);
		
		//Rect location = locateView(anchor);
//		v.setX(location.left);
//		v.setY(location.bottom);
//		
//		PopupWindow pop = new PopupWindow(context);
//		pop.setContentView(arrow);
		

			myBubble.setYPointer(screenHeight - y);

		
		switch (zone) {
//		case 0:				
//
//			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location.left - width, location.bottom - (height/2 + 15));// + anchor.getHeight()/2);
//			//mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(anchor.getX() - width), (int)anchor.getY() - height/2 + anchor.getHeight()/2);
//			
//			//pop.showAtLocation(v, Gravity.NO_GRAVITY, location.left , location.bottom + anchor.getHeight()/2);
//			break;
//
//		case 90:			
//
//			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location.left - width/2 + anchor.getWidth()/2, location.bottom + anchor.getHeight() - 10);
//			//pop.showAtLocation(v, Gravity.NO_GRAVITY, location.left - width/2 + anchor.getWidth()/2, location.bottom + anchor.getHeight());
//			//mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(anchor.getX() - width/2 + anchor.getWidth()/2), (int)anchor.getY() + anchor.getHeight());
//			break;
//
//		case 180:			
//
//			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location.left + anchor.getWidth(), location.bottom - (height/2 + 5));// + anchor.getHeight()/2);
//			//pop.showAtLocation(v, Gravity.NO_GRAVITY, location.left + anchor.getWidth() , location.bottom + anchor.getHeight()/2);
//			//mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(anchor.getX() + anchor.getWidth()), (int)anchor.getY() - height/2 + anchor.getHeight()/2);
//
//			break;
//
//
//		case 270:
//
//			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location.left - width/2 + anchor.getWidth()/2, location.bottom - (height + 15));
//			//pop.showAtLocation(v, Gravity.NO_GRAVITY, location.left - width/2 + anchor.getWidth()/2, location.bottom);
//			//mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(anchor.getX() - width/2 + anchor.getWidth()/2), (int)anchor.getY()- height);
//			break;
//
//		default:
//			break;
//		}
		
		case 0:
			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(x - width + 20), (int)(y - (height/2 + 5)));
			break;

		case 90:			

			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(x - width/2 + 15), (int)(y + 30 - 10));
			break;

		case 180:			

			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(x + 30), (int)(y - (height/2 + 5)));// + anchor.getHeight()/2);

			break;


		case 270:

			mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (int)(x - width/2 + 15), (int)(y - (height)));
			break;

		default:
			break;
		}
		

	}

	public Rect locateView(View v)
	{
	    int[] loc_int = new int[2];
	    if (v == null) return null;
	    try
	    {
	        v.getLocationOnScreen(loc_int);
	    } catch (NullPointerException npe)
	    {
	        //Happens when the view doesn't exist on screen anymore.
	        return null;
	    }
	    Rect location = new Rect();
	    location.left = loc_int[0];
	    location.top = loc_int[1];
	    location.right = location.left + v.getWidth();
	    location.bottom = location.top + v.getHeight();
	    return location;
	}

	public void showInCenter(View anchor) {
		preShow();

		//mWindow.setWidth(350);
		mWindow.showAtLocation(anchor, Gravity.LEFT, (int)anchor.getX(), (int)anchor.getY());

	}



	public void setDimensionByGridLocation(){


		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		//		mLocationInfos.dismiss();
		DisplayMetrics metrics = new DisplayMetrics(); 
		//int orientation = display.getOrientation();
		//((Activity)context).getWindowManager()
		wm.getDefaultDisplay().getMetrics(metrics); 

		if(metrics.densityDpi >= 213 && metrics.densityDpi <= 219) {
			width = 410;
			height = 410;
		}else if(metrics.densityDpi == 240) {
			width = 390;
			height = 400;
		}
		else if(metrics.densityDpi == 320){
			width = 650;
			height = 650;
		}
		else if(metrics.densityDpi > 219 && metrics.densityDpi < 480) {
//			width = 470;
//			height = 420;
			width = 540;
			height = 600;
		}else if(metrics.densityDpi >= 480) {
			width = 500;
			height = 650;
		}else {
			width = 360;
			height = 400;
		}


		if(point.getIllustration() != null) {
			height += (int) context.getResources().getDimension(R.dimen.infoWindow_height);
		}


		if(point.getText() == null || point.getText().isEmpty())
			height -= 50;

		//		if (event.getLink_type() ==null || location.getLink_type().isEmpty()) 
		//			height -= 50;


		setWidht(width);
		setHeight(height);


	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidht(int width) {
		this.width = width;
	}


	public void setHeight(int height) {
		this.height = height;
	}

	

	public void setOnDismissListener(QuickAction.OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	@Override
	public void onDismiss() {
		if (mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}

	/**
	 * Listener for item click
	 *
	 */
	public interface OnActionItemClickListener {
		public abstract void onItemClick(QuickAction source, int pos, int actionId);
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}

	public boolean isShown(){
		return mWindow.isShowing();
	}



}

