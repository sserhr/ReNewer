/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.publish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.study.ranhuo.renewer.R;
import com.study.ranhuo.renewer.home.Home_FragmentActivity;
import com.study.ranhuo.renewer.util.Bimp;
import com.study.ranhuo.renewer.util.FileUtils;
import com.study.ranhuo.renewer.util.ImageItem;
import com.study.ranhuo.renewer.util.PublicWay;
import com.study.ranhuo.renewer.util.Res;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class publishActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private ImageView publishImage;
	private ImageView publishCancelImage;
	private EditText title_edit;
	private EditText description_edit;
	private EditText category_edit;
	private EditText price_edit;
	private static String title;
	private static String description;
	private static String category;
	private static String price;
	private static AVUser user;
	private static ProgressDialog progressDialog;
	private static Context mContext;
	private static Home_FragmentActivity home_fragmentActivity;
	//private AVObject avObject = new AVObject("myGoods");
	public static Bitmap bimap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);//加号那个图标
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_publish, null);//实例化此布局
		setContentView(parentView);//加载view
		Init();
	}

	public void Init() {

		mContext = this;
		pop = new PopupWindow(publishActivity.this);//弹窗类

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);//加载弹窗里面的布局。

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);//设置为true才有点击事件
		pop.setOutsideTouchable(true);//能否点击popwindow外面
		pop.setContentView(view);//加载view

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);//弹窗的布局
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(publishActivity.this,
						AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		title_edit = (EditText) findViewById(R.id.publish_edit_title);
		description_edit = (EditText) findViewById(R.id.publish_edit_description);
		//category_edit = (EditText) findViewById(R.id.publish_edit_category);
		price_edit = (EditText) findViewById(R.id.publish_edit_price);

		//leancloudHelper = new LeancloudUploadImage();
		publishImage = (ImageView) findViewById(R.id.img_publish_commit);
		publishImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//                Log.d("hr", "temp Test2 :" + Bimp.tempSelectBitmap.size());
//                leancloudHelper.addImage2AVObject();
				title = title_edit.getText().toString();
				//Log.d("hrPublish ", "title " + title);
				description = description_edit.getText().toString();
				//Log.d("hrPublish ", "description" + description);
				//category = category_edit.getText().toString();
				//Log.d("hrPublish ", "category" + category);
				price = price_edit.getText().toString();
				//Log.d("hrPublish ", "price" + price);

				if(title.isEmpty()){
					showTitleERR();
					return;
				}
				if(description.isEmpty()){
					showDescriptionERR();
					return;
				}
				if(price.isEmpty()){
					showPriceERR();
					return;
				}
				if(Bimp.tempSelectBitmap.isEmpty()){
					showImageERR();
					return;
				}
				uploadeImageList uploadeImageList = new uploadeImageList();
				uploadeImageList.execute();

//				AVObject testObject = new AVObject("TestObject");
//				try {
//					AVFile avFile = AVFile.withAbsoluteLocalPath(Bimp.tempSelectBitmap.get(0).getImageName(Bimp.tempSelectBitmap.get(0).getImagePath()), Bimp.tempSelectBitmap.get(0).getImagePath());
//					testObject.put("Image", avFile);
//					testObject.saveInBackground();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		});
		//testimage = (ImageView) findViewById(R.id.img_test);
		publishCancelImage = (ImageView) findViewById(R.id.img_publish_cancle);
		publishCancelImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//				Intent intent = new Intent(publishActivity.this, Home_FragmentActivity.class);
//				startActivity(intent);

				Toast.makeText(publishActivity.this, "Publish cancel",
						Toast.LENGTH_SHORT).show();
                Bimp.tempSelectBitmap.clear();
                Intent intent = new Intent(publishActivity.this, Home_FragmentActivity.class);
                startActivity(intent);
				finish();
//				AVQuery<AVObject> avQuery = AVQuery.getQuery("Goods");
//				avQuery.whereEqualTo("Publisher", user.getCurrentUser());
//				avQuery.findInBackground(new FindCallback<AVObject>() {
//					@Override
//					public void done(List<AVObject> list, AVException e) {
//						for(AVObject object : list){
//							Log.v("hr", "MyPublish: " + object.getObjectId());
//						}
//					}
//				});
//				avQuery.findInBackground(new FindCallback<AVObject>() {
//					@Override
//					public void done(List<AVObject> list, AVException e) {
//						for(AVObject object : list){
//							List<AVFile> templist = object.getList("Images");
//							Log.v("hr", "downLoadTest: " + templist.size());
//							for(AVFile ele : templist){
//								Log.v("hr", "url: " + ele.getUrl());
//								ele.getDataInBackground(new GetDataCallback() {
//									@Override
//									public void done(byte[] bytes, AVException e) {
//										Log.v("hr", "Image2Bitmap: " + bytes.length);
//										testimage.setImageBitmap(array2bitmap(bytes));
//										testimage.postInvalidate();
//									}
//								});
//							}
//						}
//					}
//
//				});


			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(publishActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {

					Intent intent = new Intent(publishActivity.this,
							GalleryActivity.class);//图片预览时候的activity.
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 3) {
				return 3;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 3) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
				//Log.v("hr", position + "hrPath: " + Bimp.tempSelectBitmap.get(position).getImagePath().toString());
				//Log.v("hr", position +  "hrName: " + Bimp.tempSelectBitmap.get(position).getImageName(Bimp.tempSelectBitmap.get(position).getImagePath().toString()).toString());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						adapter.notifyDataSetChanged();
						break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case TAKE_PICTURE:
				if (Bimp.tempSelectBitmap.size() < 3 && resultCode == RESULT_OK) {

					String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					FileUtils.saveBitmap(bm, fileName);

					ImageItem takePhoto = new ImageItem();
					takePhoto.setBitmap(bm);
                    takePhoto.setImagePath(FileUtils.SDPATH + fileName+".JPEG");
					Bimp.tempSelectBitmap.add(takePhoto);
					Log.d("hrcameral", "cameraPath: " + Bimp.tempSelectBitmap.get(0).getImagePath());
				}
				break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}

	public Bitmap array2bitmap(byte[] bytes) {
		Bitmap bitmapTemp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		Log.v("hr", "bitmapTemp" + bitmapTemp.getWidth());
		return bitmapTemp;
	}

	class uploadeImageList extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected void onPreExecute() {

            progressDialogShow();
		}

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (Bimp.tempSelectBitmap != null) {
                List<AVFile> fileList = new LinkedList<AVFile>();
                for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                    try {
                        AVFile tempAFfile = AVFile.withAbsoluteLocalPath(Bimp.tempSelectBitmap.get(i).getImageName(Bimp.tempSelectBitmap.get(i).getImagePath()), Bimp.tempSelectBitmap.get(i).getImagePath());
                        tempAFfile.save();
                        fileList.add(tempAFfile);

                    } catch (IOException e) {
                        e.printStackTrace();
                        //Log.d("hr1234567", "bug");
                    } catch (AVException e) {
                        e.printStackTrace();
                        //Toast.makeText(publishActivity.this, "Publish failed, please check the network connection.", Toast.LENGTH_SHORT).show();
                        //Log.d("hr1234567", "bug1");
                    }


                }
                AVObject goods = new AVObject("Goods");
                goods.addAll("Images", fileList);
                goods.add("Title", title);
                goods.add("Discription", description);
                //int category = Integer.parseInt(category_edit.getText().toString());
                goods.add("Catergory", category);
                //float price = Float.parseFloat(price_edit.getText().toString());
                goods.add("Price", price);
                goods.add("Publisher", user.getCurrentUser());

                goods.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            progressDialog.dismiss();
                            Toast.makeText(publishActivity.this, "Publish successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(publishActivity.this, Home_FragmentActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(publishActivity.this, "Publish failed, please check the network connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
            return true;
        }

    }

	public void showTitleERR() {
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Please input a Title!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		}).show();

	}

	public void showDescriptionERR(){
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Please input some description!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		}).show();
	}

	public void showPriceERR(){
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Please give a price!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		}).show();
	}

	public void showImageERR(){
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage("Please choose some image!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		}).show();
	}

	private static void progressDialogDismiss() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	private static void progressDialogShow() {
		progressDialog = ProgressDialog
				.show(mContext,
						mContext.getResources().getText(
								R.string.dialog_message_title),
						mContext.getResources().getText(
								R.string.dialog_text_wait), true, false);
	}




}

