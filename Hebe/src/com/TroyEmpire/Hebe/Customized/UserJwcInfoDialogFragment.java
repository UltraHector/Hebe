package com.TroyEmpire.Hebe.Customized;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;

import com.TroyEmpire.Hebe.Activities.IExamScoreDisplayActivity;
import com.TroyEmpire.Hebe.Activities.R;
import com.TroyEmpire.Hebe.Constant.Constant;
import com.TroyEmpire.Hebe.Constant.JwcAction;
import com.TroyEmpire.Hebe.Entities.ExamScore;
import com.TroyEmpire.Hebe.IServices.IHebeCommonService;
import com.TroyEmpire.Hebe.IServices.IScheduleService;
import com.TroyEmpire.Hebe.Services.HebeCommonService;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
// API = 8 and above
public class UserJwcInfoDialogFragment extends DialogFragment {

	private Activity activity;
	private IScheduleService scheduleService;
	private View dialogView;
	private SharedPreferences userJwcInfo;
	private JwcAction jwcAction;
	private IHebeCommonService hebeCommonService = new HebeCommonService();
	private int netJobfeedBack;
	private Response response;

	@SuppressLint("ValidFragment")
	public UserJwcInfoDialogFragment(Activity activity, JwcAction jwcAction,
			IScheduleService scheduleService) {
		this.activity = activity;
		this.jwcAction = jwcAction;
		this.scheduleService = scheduleService;
		this.userJwcInfo = activity.getSharedPreferences(
				Constant.USER_JWC_INFO, Context.MODE_PRIVATE);
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialogView = inflater.inflate(R.layout.dialog_jwc_user_info, null);
		builder.setView(dialogView)
				.setTitle(Constant.NEED_INPUT_JWC_USER_INFO)
				// click the confirm button and call the function
				.setPositiveButton(R.string.confirm,
						new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Do nothing , we will override it
							}
						})
				.setNegativeButton(R.string.concel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								UserJwcInfoDialogFragment.this.getDialog()
										.cancel();
							}
						});
		// ----------------------------------------------------------
		// Override the onclick() of positive button API 8 and above
		// ----------------------------------------------------------
		final AlertDialog dialog_ready = builder.create();
		dialog_ready.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				setDefaultValue();
				Button b = dialog_ready
						.getButton(DialogInterface.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// decide whether to save the user's info
						saveUserInfo();
						String validationCode = ((EditText) dialogView
								.findViewById(R.id.jwc_user_validation_code_text_view))
								.getText().toString();
						// ////////////////////////////////////////////////
						// TODO the dialog should keep if network error or
						// password error
						// ////////////////////////////////////////////////
						dismiss();
						switch (jwcAction) {
						case UPDATE_SCHEDULE:
							startUpdateSchedule(response, validationCode);
							break;
						case GET_EXAM_SCORE:
							startDisplayExamScore(response, validationCode);
							break;
						case UPDATE_COURSE_SCHEDULE:
							startUpdateCourseSchedule(response, validationCode);
							break;
						case UPDATE_EXAM_SCHEDULE:
							startUpdateExamchedule(response, validationCode);
							break;
						}
					}
				});
			}
		});
		return dialog_ready;
	}

	private void startDisplayExamScore(Response response, String validationCode) {
		Intent i3 = new Intent(this.activity, IExamScoreDisplayActivity.class);
		List<ExamScore> examScores = scheduleService.getExamScores(
				getUserCredential(), response, validationCode);
		int i = examScores.size();
		i3.putExtra(Constant.NUMBER_OF_EXAM_SCORES, i);
		for (ExamScore examScore : examScores) {
			i3.putExtra("examScores_" + (i--), examScore);
		}
		startActivity(i3);
	}

	private void startUpdateSchedule(Response response, String validationCode) {
		this.scheduleService.updateUserSchedule(getUserCredential(), response,
				validationCode);
		Log.d("Notice", "Update with default password successfully");
	}

	private void startUpdateCourseSchedule(Response response,
			String validationCode) {
		this.scheduleService.updateUserCourseSchedule(getUserCredential(),
				response, validationCode);
		Log.d("Notice", "Update with default password successfully");
	}

	private void startUpdateExamchedule(Response response, String validationCode) {
		this.scheduleService.updateUserExamSchedule(getUserCredential(),
				response, validationCode);
		Log.d("Notice", "Update with default password successfully");
	}

	private void setDefaultValue() {
		// set the default value
		EditText userAccountNumber = (EditText) (dialogView
				.findViewById(R.id.jwc_user_account_number));
		EditText userPassword = (EditText) (dialogView
				.findViewById(R.id.jwc_user_password));
		CheckBox saveInfo = (CheckBox) (dialogView
				.findViewById(R.id.jwc_user_checkbox));
		if (userJwcInfo.contains(Constant.USER_JWC_ACCOUNT_NUMBER))
			userAccountNumber.setText(userJwcInfo.getString(
					Constant.USER_JWC_ACCOUNT_NUMBER, ""));
		if (userJwcInfo.contains(Constant.USER_JWC_PASSWORD)) {
			userPassword.setText(userJwcInfo.getString(
					Constant.USER_JWC_PASSWORD, ""));
			saveInfo.setChecked(true);
		}

		
		final String tempFolderPath = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ Constant.HEBE_TEMP;
		Thread child = new Thread() {
			@Override
			public void run() {
				try {
					// set the validation imge
					Connection jwcLoginUrlConnection = Jsoup
							.connect(Constant.JWC_LOGIN_WINDOW_URL);
					Connection jwcValidationCode = Jsoup
							.connect(Constant.JWC_VALIDATION_CODE_URL);

					Connection.Response jwcLoginUrlGetResponse = jwcLoginUrlConnection
							.method(Method.GET).execute();
					response = jwcLoginUrlGetResponse;

					// Temple save the validation code
					File tempFolder = new File(tempFolderPath);
					if (!tempFolder.exists())
						tempFolder.mkdirs();
					File tempValidationCode = new File(tempFolderPath
							+ "/temp.png");
					if (!tempValidationCode.exists())
						tempValidationCode.createNewFile();
					FileUtils.writeByteArrayToFile(tempValidationCode,
							jwcValidationCode.cookies(response.cookies())
									.ignoreContentType(true).execute()
									.bodyAsBytes());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		child.start();
		try {
			child.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeFile(tempFolderPath+ "/temp.png");
		ImageView validationView = (ImageView) dialogView
				.findViewById(R.id.jwc_user_validation_code_image_view);
		validationView.setImageBitmap(bitmap);
	}

	// Get the user's JWC information credentials
	private Map<String, String> getUserCredential() {
		EditText userAccountNumberView = (EditText) (dialogView
				.findViewById(R.id.jwc_user_account_number));
		EditText userPasswordView = (EditText) (dialogView
				.findViewById(R.id.jwc_user_password));
		Map<String, String> userJwcCredential = new HashMap<String, String>();
		String userAccountNumber = userAccountNumberView.getText().toString();
		String userPassword = userPasswordView.getText().toString();
		userJwcCredential.put(Constant.LOGIN_WINDOW_TAG_USER_ACCOUNT_NUMBER,
				userAccountNumber);
		userJwcCredential.put(Constant.LOGIN_WINDOW_TAG_PASSWORD, userPassword);
		return userJwcCredential;
	}

	private void saveUserInfo() {
		EditText userAccountNumber = (EditText) (dialogView
				.findViewById(R.id.jwc_user_account_number));
		EditText userPassword = (EditText) (dialogView
				.findViewById(R.id.jwc_user_password));
		CheckBox saveInfo = (CheckBox) (dialogView
				.findViewById(R.id.jwc_user_checkbox));
		// Save the Jwc input by the user into the system
		if (saveInfo.isChecked()) {
			SharedPreferences.Editor editor = userJwcInfo.edit();
			editor.putString(Constant.USER_JWC_ACCOUNT_NUMBER,
					userAccountNumber.getText().toString());
			editor.putString(Constant.USER_JWC_PASSWORD, userPassword.getText()
					.toString());
			editor.commit(); // save the data
		}
	}

}