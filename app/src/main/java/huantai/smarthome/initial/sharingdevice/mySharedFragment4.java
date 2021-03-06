package huantai.smarthome.initial.sharingdevice;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizUserInfo;

import huantai.smarthome.initial.CommonModule.GosConstant;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.DateUtil;
import huantai.smarthome.initial.view.SlideListView2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class mySharedFragment4 extends Fragment {

	// 定义俩个整形值用来区分当前要显示的是哪个view对象
	// 如果是1的话就共享列表， 2的话就是受邀列表
	private int mytpye = -1;
	private myadapter myadapter;
	private String deviceID;
	private String token;
	private String uid;
	private String productname;
	private TextView shareddeviceproductname;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// 动态找到布局文件，再从这个布局中find出TextView对象
		View contextView = inflater.inflate(R.layout.activity_gos_shared_list, container, false);
		SlideListView2 mListView = (SlideListView2) contextView.findViewById(R.id.mysharedlist);
		mListView.initSlideMode(SlideListView2.MOD_RIGHT);
		// 如果是共享状态界面的话需要显示
		LinearLayout addshared = (LinearLayout) contextView.findViewById(R.id.addshared);

		shareddeviceproductname = (TextView) contextView.findViewById(R.id.shareddeviceproductname);
		Bundle arguments = getArguments();
		productname = arguments.getString("productname");

		if (GosConstant.mybindUsers.size() == 0) {
			shareddeviceproductname.setText(getResources().getString(R.string.have_no_bound_users));
		} else {
			shareddeviceproductname.setText(productname + getResources().getString(R.string.bounduser));
		}

		shareddeviceproductname.setVisibility(View.VISIBLE);
		myadapter = new myadapter();
		mListView.setAdapter(myadapter);
		initdata();
		return contextView;
	}

	public myadapter getmyadapter() {
		return myadapter;
	}

	private void initdata() {
		SharedPreferences spf = getActivity().getSharedPreferences("set", Context.MODE_PRIVATE);
		token = spf.getString("Token", "");
		uid = spf.getString("Uid", "");

		deviceID = getArguments().getString("deviceid");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// listview的adapter，通过刷新的bound对象的属性值来判断当前应该现实的是什么
	class myadapter extends BaseAdapter {

		private String uid;

		@Override
		public int getCount() {
			return GosConstant.mybindUsers.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (GosConstant.mybindUsers.size() == 0) {
				shareddeviceproductname.setText(getResources().getString(R.string.have_no_bound_users));
			} else {
				shareddeviceproductname.setText(productname + getResources().getString(R.string.bounduser));
			}
			View view = null;

			view = View.inflate(getActivity(), R.layout.gos_shared_by_me_activity, null);
			final TextView tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);

			TextView tvDeviceMac = (TextView) view.findViewById(R.id.tvDeviceMac);

			TextView tvDeviceStatus = (TextView) view.findViewById(R.id.tvDeviceStatus);

			TextView delete2name = (TextView) view.findViewById(R.id.delete2name);

			TextView delete3name = (TextView) view.findViewById(R.id.delete3name);

			delete3name.setVisibility(View.GONE);

			delete2name.setText(getResources().getString(R.string.cancel_sharing));

			delete2name.setVisibility(View.VISIBLE);

			delete2name.setText(getResources().getString(R.string.mycancel));

			tvDeviceStatus.setVisibility(View.GONE);

			GizUserInfo userInfo = GosConstant.mybindUsers.get(position);

			uid = userInfo.getUid();

			String email = userInfo.getEmail();

			String phone = userInfo.getPhone();

			String username = userInfo.getUsername();

			String remark = userInfo.getRemark();
			String deviceBindTime = userInfo.getDeviceBindTime();

			deviceBindTime = DateUtil.utc2Local(deviceBindTime);
			tvDeviceMac.setText(deviceBindTime);

			if (!TextUtils.isEmpty(uid) && !uid.equals("null")) {
				String myuid = uid.substring(0, 3) + "***" + uid.substring(uid.length() - 4, uid.length());
				tvDeviceName.setText(myuid);

			}

			if (!TextUtils.isEmpty(email) && !email.equals("null")) {

				tvDeviceName.setText(email);

			}

			if (!TextUtils.isEmpty(phone) && !phone.equals("null")) {

				tvDeviceName.setText(phone);

			}

			if (!TextUtils.isEmpty(username) && !username.equals("null")) {

				tvDeviceName.setText(username);

			}

			if (!TextUtils.isEmpty(remark) && !remark.equals("null")) {

				tvDeviceName.setText(remark);

			}

			delete2name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					quitAlert(getActivity(), tvDeviceName.getText().toString(), GosConstant.mybindUsers.get(position).getUid());
				}
			});

			return view;
		}

	}

	protected void quitAlert(Context context, String username, final String uid2) {
		final Dialog dialog = new AlertDialog.Builder(getActivity()).setView(new EditText(getActivity())).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_gos_quit);

		LinearLayout llNo, llSure;
		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);

		TextView view3 = (TextView) window.findViewById(R.id.textView3);
		view3.setVisibility(View.VISIBLE);
		TextView tv = (TextView) window.findViewById(R.id.tv_prompt);

		String userstring = getResources().getString(R.string.deleteuserpremiss);
		String[] split = userstring.split("xxx");

		tv.setText(split[0] + username + split[1]);

		llNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GizDeviceSharing.unbindUser(token, deviceID, uid2);
				dialog.cancel();
			}
		});
	}

}
