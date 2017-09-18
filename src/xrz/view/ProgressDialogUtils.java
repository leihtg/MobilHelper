package xrz.view;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {

	public static ProgressDialog createProgressDialog(Context c,String content){
		ProgressDialog dialog=new ProgressDialog(c);
		dialog.setMessage(content);
		return dialog;
	}
}
