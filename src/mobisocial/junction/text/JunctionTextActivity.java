package mobisocial.junction.text;

import java.net.URI;

import mobisocial.nfc.Nfc;
import mobisocial.nfc.Nfc.NdefFactory;

import org.json.JSONObject;

import edu.stanford.junction.Junction;
import edu.stanford.junction.JunctionException;
import edu.stanford.junction.JunctionMaker;
import edu.stanford.junction.SwitchboardConfig;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class JunctionTextActivity extends Activity {
	private static final String TAG = "junctiontext";
	private Nfc mNfc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mNfc = new Nfc(this);

        joinOrDie();
        buildUi();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mNfc.onResume(this);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mNfc.onPause(this);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	if (mNfc.onNewIntent(this, intent)) return;
    }
    
    private void joinOrDie() {
        try {
        	SwitchboardConfig config = new XMPPSwitchboardConfig("prpl.stanford.edu");
        	JunctionMaker jm = JunctionMaker.getInstance(config);
        	
        	URI invitation = jm.generateSessionUri();
        	URI webInvite = URI.create(Junction.toWebInvite("http://prpl.stanford.edu/junction/text", invitation.toString()));
        	mNfc.share(NdefFactory.fromUri(webInvite));
            jm.newJunction(invitation, mStatusUpdater);
        	// 	TODO: Junction.from(Uri invitation).bind(mRunner);
        } catch (JunctionException e) {
        	Log.e(TAG, "Could not connect to junction.");
        	toast("Could not connect to Junction");
        	finish();
        }
    }
    
    private void buildUi() {
    	findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String update = ((EditText)findViewById(R.id.status)).getText().toString();
				Intent result = new Intent();
				result.putExtra(Intent.EXTRA_TEXT, update);
				setResult(RESULT_OK, result);
				finish();
			}
		});
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