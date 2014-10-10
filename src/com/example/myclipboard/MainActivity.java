package com.example.myclipboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager; // ※ androix.text.～ ではなく android.context.～ を import
import android.content.Intent;
import android.content.ClipData.Item;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	EditText mEdit;
	TextView mText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mEdit = (EditText)findViewById(R.id.editText1);
		mText = (TextView)findViewById(R.id.textView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// コピー処理
	public void buttonMethodCopy(View button){
		try{
			// ClipboardManager取得
			ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
			
			// 文字列取得
			String s = mEdit.getText().toString();
			
			// お試し用インテント
			Intent intent = new Intent(this, SubActivity.class);
			
			// ClipData構築（お試しで複数構築してみる）
			ClipData data = ClipData.newPlainText("sample data", s);
			data.addItem(new ClipData.Item("ppppp"));
			data.addItem(new ClipData.Item(intent)); // インテント混在
			cm.setPrimaryClip(data);
		}
		catch(Exception ex){
			mText.setText("Error: " + ex.toString());
		}
	}
	
	// ペースト処理
	public void buttonMethodPaste(View button){
		try{
			// ClipboardManager取得
			ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
			if(cm == null)throw new Exception("Clipboard Manager not found");
			
			// ClipData取得
			ClipData data = cm.getPrimaryClip();
			if(data == null)throw new Exception("ClipData not found");

			// 概要取得
			String s = "";
			s += "--------------------\n";
			s += "Summary: " + data.toString() + "\n";
			s += "--------------------\n";
			
			// アイテム数取得
			s += "ItemCount: " + data.getItemCount() + "\n";
			
			// 全アイテム取得
			for(int i = 0; i < data.getItemCount(); i++){
				ClipData.Item item = data.getItemAt(i);
				// getTextを試す
				CharSequence cs = item.getText();
				if(cs != null){
					s += "Item[" + i + "]: " + cs.toString() + "\n";
					continue;
				}
				// getIntentを試す
				Intent intent = item.getIntent();
				if(intent != null){
					s += "Item[" + i + "]: " + intent.toString() + "\n";
					continue;
				}
			}
			
			// 表示
			mText.setText(s);
		}
		catch(Exception ex){
			mText.setText("Error: " + ex.toString());
		}
	}
	
	// ペースト処理２（Intentを検出して起動）
	public void buttonMethodPaste2(View button){
		try{
			// ClipboardManager取得
			ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
			if(cm == null)throw new Exception("Clipboard Manager not found");
			
			// ClipData取得
			ClipData data = cm.getPrimaryClip();
			if(data == null)throw new Exception("ClipData not found");

			// 全アイテム取得
			for(int i = 0; i < data.getItemCount(); i++){
				ClipData.Item item = data.getItemAt(i);
				// getIntentを試す
				Intent intent = item.getIntent();
				if(intent != null){
					mText.setText("Launch intent of ClipItem[" + i + "]");
					this.startActivity(intent);
					return;
				}
			}
			
			// Intentが見つからなかった
			mText.setText("Intent not found in ClipData");
		}
		catch(Exception ex){
			mText.setText("Error: " + ex.toString());
		}
	}
}
