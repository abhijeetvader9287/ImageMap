package com.lurencun.imagemap;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lurencun.imagemap.internal.Bubble;
import com.lurencun.imagemap.internal.Bubble.OnBubbleClickListener;
import com.lurencun.imagemap.internal.Bubble.OnShapeClickListener;
import com.lurencun.imagemap.internal.ImageMap;
import com.lurencun.imagemap.internal.Shape;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
public class ImageMapTestActivity extends Activity implements OnBubbleClickListener, OnShapeClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final ImageMapLayout imageMapView = (ImageMapLayout) findViewById(R.id.map);
        //imageMapView.setImageResource(R.drawable.usamap);
        imageMapView.setImageResource(R.drawable.gridmap);

        loadMap(imageMapView,"gridmap");
     //   loadMap(imageMapView,"usamap");
        imageMapView.setOnBubbleClickeListener(this);
        imageMapView.setOnShapeClickeListener(this);
    }

    private void loadMap(ImageMapLayout imageMapView,String mapname_) {

        boolean loading = false;
        try {
            XmlResourceParser xpp = getResources().getXml(R.xml.maps);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    // Start document
                    //  This is a useful branch for a debug log if
                    //  parsing is not working
                } else if (eventType == XmlPullParser.START_TAG) {
                    String tag = xpp.getName();

				 	if (tag.equalsIgnoreCase("map")) {
						String mapname = xpp.getAttributeValue(null, "name");
						if (mapname !=null) {
							if (mapname.equalsIgnoreCase(mapname_)) {
								loading=true;
							}
						}
					}
                    if (loading) {
                        if (tag.equalsIgnoreCase("area")) {
                            String shape = xpp.getAttributeValue(null, "shape");
                            String coords = xpp.getAttributeValue(null, "coords");
                            String id = xpp.getAttributeValue(null, "id");
                            // as a name for this area, try to find any of these
                            // attributes
                            //  name attribute is custom to this impl (not standard in html area tag)
                            String name = xpp.getAttributeValue(null, "name");
                            if (name == null) {
                                name = xpp.getAttributeValue(null, "title");
                            }
                            if (name == null) {
                                name = xpp.getAttributeValue(null, "alt");
                            }
                            if ((shape != null) && (coords != null)) {
                                Shape shape1 = new Shape(name + id, coords);
                                int id3 = imageMapView.addShape(shape1);
                                TextView view1 = new TextView(this);
                                view1.setText(name);
                                view1.setPadding(10, 10, 10, 10);
                                view1.setBackgroundColor(Color.BLACK);
                                imageMapView.addBubble(view1, id3);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    String tag = xpp.getName();
                    if (tag.equalsIgnoreCase("map")) {
                        loading = false;
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException xppe) {
            // Having trouble loading? Log this exception
        } catch (IOException ioe) {
            // Having trouble loading? Log this exception
        }
    }

    @Override
    public void onShapeClick(ImageMap imageMap, int shapeId) {

        imageMap.showBubble(true, shapeId);
    }

    @Override
    public void onBubbleClick(Bubble bubble, int shapeId) {

        Toast.makeText(this, "Click Bubble view:shapeid=" + shapeId + " data=" + bubble.data, Toast.LENGTH_SHORT).show();
    }
}