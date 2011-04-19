package mobisocial.junction.text;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.jivesoftware.smack.util.Base64;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

/**
 * An inefficient implementation of a file transfer.
 *
 */
class FileReceiver {
	private final JSONObject mFileReference;
	
	public static boolean takeIfYouWant(JSONObject obj) {
		if (obj.has("file:b64")) {
			new FileReceiver(obj).consume();
		}
		return false;
	}
	
	private FileReceiver(JSONObject obj) {
		mFileReference = obj;
	}
	
	public void consume() {
		try {
			String fileName = "demo";
            File root = Environment.getExternalStorageDirectory();
            ByteArrayInputStream byteStream = new ByteArrayInputStream(
            		Base64.decode(mFileReference.optString("file:b64")));
            FileOutputStream f = new FileOutputStream(new File(root, fileName));

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = byteStream.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (Exception e) {
            Log.d("Downloader", e.getMessage());
        }
	}
}