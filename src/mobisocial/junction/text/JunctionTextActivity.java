package mobisocial.junction.text;

import java.net.URI;

import org.json.JSONObject;

import edu.stanford.junction.JunctionException;
import edu.stanford.junction.JunctionMaker;
import edu.stanford.junction.SwitchboardConfig;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class JunctionTextActivity extends Activity {
	private static final String TAG = "junctiontext";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        joinOrDie();
        buildUi();
    }
    
    private void joinOrDie() {
        try {
        	URI invitation = URI.create("junction://prpl.stanford.edu/jxtext");
            SwitchboardConfig config = new XMPPSwitchboardConfig("prpl.stanford.edu");
        	JunctionMaker.getInstance(config).newJunction(invitation, mStatusUpdater);
        	// 	TODO: Junction.from(Uri invitation).bind(mRunner);
        } catch (JunctionException e) {
        	Log.e(TAG, "Could not connect to junction.");
        	toast("Could not connect to Junction");
        	finish();
        }
    }
    
    private void buildUi() {
    	
    }
    
    private JunctionActor mStatusUpdater = new JunctionActor() {
		
		@Override
		public void onMessageReceived(MessageHeader header, final JSONObject msg) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					((EditText)findViewById(R.id.status)).setText(msg.optString("text"));					
				}
			});
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