package biome.fresnotes;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Drew on 2/26/2017.
 */

public class SourceSansRegularTextView extends AppCompatTextView{

    public SourceSansRegularTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"SourceSansPro-Regular.ttf"));
    }
}
