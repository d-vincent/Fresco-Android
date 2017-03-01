package biome.fresco;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Drew on 2/26/2017.
 */

public class SourceSansProBoldTextView extends TextView {
    public SourceSansProBoldTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"SourceSansPro-Bold.ttf"));
    }
}
