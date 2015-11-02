package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.ui.adapter.AddressAdapter;
import com.plmt.boommall.ui.utils.ListItemClickHelp;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.OnSheetItemClickListener;
import com.plmt.boommall.ui.view.iosdialog.ActionSheetDialog.SheetItemColor;

public class AddressListActivity extends Activity implements OnClickListener,
		ListItemClickHelp {

	public static final String ORIGIN_FROM_ORDER_KEY = "com.order";

	public static final String ORIGIN_FROM_ACCOUNT_KEY = "com.account";

	private String mNowAction = ORIGIN_FROM_ACCOUNT_KEY;

	private Context mContext;

	private MultiStateView mMultiStateView;
	private ImageView mBackIv;
	private Button mAddAddressBtn;

	private ListView mAddressLv;
	private ArrayList<Address> mAddresslist = new ArrayList<>();
	private AddressAdapter mAdapter;

	private Address mAddress;
	
	private CustomProgressDialog mProgressDialog;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AddressLogic.ANDRESS_LIST_GET_SUC: {
				if (null != msg.obj) {
					mAddresslist.clear();
					mAddresslist
							.addAll((Collection<? extends Address>) msg.obj);
					mAdapter.initCheck();
					mAdapter.notifyDataSetChanged();
				}
				break;
			}
			case AddressLogic.ANDRESS_LIST_GET_FAIL: {

				break;
			}
			case AddressLogic.ANDRESS_LIST_GET_EXCEPTION: {

				break;
			}
			case AddressLogic.ANDRESS_DEL_SUC: {
				mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
				AddressLogic.getList(mContext, mHandler);
				break;
			}
			case AddressLogic.ANDRESS_DEL_FAIL: {

				break;
			}
			case AddressLogic.ANDRESS_DEL_EXCEPTION: {

				break;
			}
			case AddressLogic.NET_ERROR: {

				break;
			}
			default:
				break;
			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_list);
		mContext = AddressListActivity.this;
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		fetchData();
	}

	private void initView() {
		mMultiStateView = (MultiStateView) findViewById(R.id.address_list_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR)
				.findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView
								.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(),
								"Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});
		//mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

		mAddAddressBtn = (Button) findViewById(R.id.address_list_add_btn);
		mBackIv = (ImageView) findViewById(R.id.address_list_back_iv);
		mAddAddressBtn.setOnClickListener(this);
		mBackIv.setOnClickListener(this);

		mAddressLv = (ListView) findViewById(R.id.address_list_lv);
		mAdapter = new AddressAdapter(mContext, mAddresslist, this);
		mAddressLv.setAdapter(mAdapter);
		mAddressLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (ORIGIN_FROM_ORDER_KEY.equals(mNowAction)) {
					mAddress = mAddresslist.get(position);
					Intent intent = new Intent();
					intent.putExtra("username", mAddress.getUsername());
					intent.putExtra("telephone", mAddress.getTelephone());
					intent.putExtra("content", mAddress.getContent());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	private void initData() {
		mNowAction = getIntent().getAction();
	}

	private void fetchData() {
		AddressLogic.getList(mContext, mHandler);
	}

	@Override
	public void onClick(View item, View widget, int position, int which) {
		switch (which) {
		case R.id.list_address_item_edit_rl: {
			Intent intent = new Intent(AddressListActivity.this,
					AddressEditActivity.class);
			intent.setAction(AddressEditActivity.ORIGIN_FROM_EDIT_ACTION);

			Bundle bundle = new Bundle();
			bundle.putSerializable(AddressEditActivity.ADDRESS_KEY,
					mAddresslist.get(position));
			intent.putExtras(bundle);

			// intent.putExtra("id", mAddresslist.get(position).getId());
			// intent.putExtra("username", mAddresslist.get(position)
			// .getUsername());
			// intent.putExtra("content",
			// mAddresslist.get(position).getContent());
			// intent.putExtra("telephone", mAddresslist.get(position)
			// .getTelephone());
			// intent.putExtra("cn_province", mAddresslist.get(position)
			// .getCn_province());
			// intent.putExtra("cn_city",
			// mAddresslist.get(position).getCn_city());
			// intent.putExtra("cn_district", mAddresslist.get(position)
			// .getCn_district());
			// intent.putExtra("postCode", mAddresslist.get(position)
			// .getPostCode());

			startActivityForResult(intent, 600);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.list_address_item_del_rl: {
			final int tempPosition = position;
			new ActionSheetDialog(AddressListActivity.this)
					.builder()
					.setTitle(getString(R.string.is_del_address_title))
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem(getString(R.string.del), SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									mProgressDialog.show();
									AddressLogic.del(mContext, mHandler,
											mAddresslist.get(tempPosition)
													.getId());
								}
							}).show();
			// new
			// AlertDialog(AddressListActivity.this).builder().setTitle(getString(R.string.prompt))
			// .setMsg(getString(R.string.exit_str))
			// .setPositiveButton(getString(R.string.confirm), new
			// OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// AddressLogic.del(mContext, mHandler,
			// mAddresslist.get(tempPosition).getId());
			// }
			// }).setNegativeButton(getString(R.string.cancal), new
			// OnClickListener() {
			// @Override
			// public void onClick(View v) {
			//
			// }
			// }).show();
			break;
		}

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_list_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}
		case R.id.address_list_add_btn: {
			Intent intent = new Intent(AddressListActivity.this,
					AddressEditActivity.class);
			intent.setAction(AddressEditActivity.ORIGIN_FROM_ADD_ACTION);
			intent.putExtra("addressId", "0");
			startActivityForResult(intent, 500);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		default:
			break;
		}

	}

}
