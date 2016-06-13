package com.blackboard.eclipse.sdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import com.blackboard.eclipse.BbPlugin;

public class SdkManager {

	/** maps version numbers (e.g. 8.0.375) to BlackboardSdk objects */
	private Map<String, BlackboardSdk> sdks;

	public SdkManager() {
		
	}

	public BlackboardSdk getBlackboardSdk(String version) {
		if (sdks == null) {
			String path = BbPlugin.getDefault().getSdkRootDirPath();
			loadSdks(path);
		}
		return sdks.get(version);
	}

	public List<BlackboardSdk> loadSdks(String sdkRootPath) {
		sdks = new HashMap<String, BlackboardSdk>();
		if (sdkRootPath == null || sdkRootPath.equals("")) {
			return null;
		}
		File sdkDir = new File(sdkRootPath);
		if (sdkDir == null || !sdkDir.exists() || !sdkDir.isDirectory()) {
			return null;
		}
		BlackboardSdk sdk = checkDirForBlackboardJars(sdkDir);
		if (sdk != null) {
			sdks.put(sdk.getVersion(), sdk);
		} else {
			for (String path : sdkDir.list()) {
				File subDir = new File(sdkDir, path);
				if (subDir.exists() && subDir.isDirectory()) {
					sdk = checkDirForBlackboardJars(subDir);
					if (sdk != null) {
						sdks.put(sdk.getVersion(), sdk);
					}
				}
			}
		}
		return new ArrayList<BlackboardSdk>(sdks.values());
	}

	public List<BlackboardSdk> getAllSdks() {
		if (sdks == null) {
			return null;
//			String path = BbPlugin.getDefault().getSdkRootDirPath();
//			loadSdks(path);
		}
		return new ArrayList<BlackboardSdk>(sdks.values());
	}
	/**
	 * checks a directory for the existence of a bb-platform.jar file, and returns the 
	 * Blackboard SDK for the version stored in MANIFEST.MF in the jar file, or null if 
	 * it bb-platform.jar doesn't exist or is invalid. 
	 * @param dir
	 * @return blackboard SDK if valid, or null if not
	 */
	private BlackboardSdk checkDirForBlackboardJars(File dir) {
		BlackboardSdk bbSdk = null;
		File bbPlatformJar = new File(dir, "bb-platform.jar");
		if (bbPlatformJar.exists()) {
			try {
				JarFile jar = new JarFile(bbPlatformJar);
				Attributes mainAtt = jar.getManifest().getMainAttributes();
				if (mainAtt != null && mainAtt.getValue("Blackboard-Version") != null) {
					bbSdk = new BlackboardSdk((String) mainAtt.getValue("Blackboard-Version"),
							dir.getAbsolutePath());
				}
			} catch (ZipException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return bbSdk;
		
	}
}
