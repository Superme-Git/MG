package com.locojoy.mini.mt3;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.snowfish.cn.ganga.helper.SFOnlineSplashActivity;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.util.Log;

public class SplashActivity extends SFOnlineSplashActivity {

	public int getBackgroundColor() {
		return Color.WHITE;
	}

	@Override
	public void onSplashStop() {

		Intent intent = new Intent(this, GameApp.class);
		startActivity(intent);
		this.finish();
	}

	public void getSingInfo() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					SplashActivity.this.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			parseSignature(sign.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseSignature(byte[] signature) {
		try {
			CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(signature));
			String pubKey = cert.getPublicKey().toString();
			String signNumber = cert.getSerialNumber().toString();
			Log.v("aaa","signName:" + cert.getSigAlgName());
			Log.v("aaa","pubKey:" + pubKey);
			Log.v("aaa","signNumber:" + signNumber);
			Log.v("aaa","subjectDN:" + cert.getSubjectDN().toString());
			
			
		} catch (CertificateException e) {
			e.printStackTrace();
		}
	}
}
