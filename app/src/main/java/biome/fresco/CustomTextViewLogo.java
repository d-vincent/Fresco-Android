package biome.fresco;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Drew on 12/13/2016.
 */

public class CustomTextViewLogo extends TextView {

    public CustomTextViewLogo(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Montserrat-Regular.ttf"));
    }
}
