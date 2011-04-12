package mobisocial.junction.text;

import java.net.URI;

import org.json.JSONObject;

import edu.stanford.junction.JunctionException;
import edu.stanford.junction.JunctionMaker;
import edu.stanford.junction.SwitchboardConfig;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class JunctionTextActivity extends Activity {
	private static final String TAG = "junctiontext";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        URI invitation = URI.create("junction://prpl.stanford.edu/jxtext");
        SwitchboardConfig config = new XMPPSwitchboardConfig("prpl.stanford.edu");
        try {
        	JunctionMaker.getInstance(config).newJunction(invitation, mStatusUpdater);
        	// 	TODO: Junction.from(Uri invitation).bind(mRunner);
        } catch (JunctionException e) {
        	Log.e(TAG, "Could not connect to junction.");
        	toast("Could not connect to Junction");
        }
    }
    
    private JunctionActor mStatusUpdater = new JunctionActor() {
		
		@Override
		public void onMessageReceived(MessageHeader arg0, JSONObject msg) {
			toast("got " + msg.optString("text"));
		}
	};
	
	private void toast(final String text) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(JunctionTextActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
    
    
}