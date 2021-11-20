package cat.urv.miv.mivandroid2d;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLBitmapFontManager {

    private Context context;
    private String ns = null;

    public XMLBitmapFontManager(Context context){
        this.context = context;
    }

    public List<FontData> parse (int text_id) {
        InputStream tFile = context.getResources().openRawResource(text_id);
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(tFile, null);
            parser.nextTag();
            return readFeed(parser);

        } catch (XmlPullParserException xne) {
            System.out.println(xne);
        } catch (IOException iox) {
            System.out.println(iox);
        }


        try {
            tFile.close();
        } catch (IOException ioe) {
            System.out.println("Error closing font file");
        }
        return null;
    }

    private List<FontData> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        List<FontData> entries = null;
        parser.require(XmlPullParser.START_TAG, ns, "font");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("chars")) {
                entries = readChars(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "font");
        return entries;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }


    }

    // Processes link tags in the feed.
    private List<FontData> readChars (XmlPullParser parser) throws IOException, XmlPullParserException {

        List<FontData> entries = new ArrayList<>();
        int ID;
        char character;
        float y;
        float x;
        float width;
        float height;

        parser.require(XmlPullParser.START_TAG, ns, "chars");
        int num_entries = Integer.parseInt(parser.getAttributeValue(null, "count"));
        int entries_counter = 0;

        while (parser.next() != XmlPullParser.END_TAG && entries_counter < num_entries) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("char")) {

                parser.require(XmlPullParser.START_TAG, ns, "char");
                ID = Integer.parseInt(parser.getAttributeValue(null, "id"));
                x = Float.parseFloat(parser.getAttributeValue(null, "x"));
                y = Float.parseFloat(parser.getAttributeValue(null, "y"));
                width = Float.parseFloat(parser.getAttributeValue(null, "width"));
                height = Float.parseFloat(parser.getAttributeValue(null, "height"));
                parser.getAttributeValue(null, "xoffset");
                parser.getAttributeValue(null, "yoffset");
                parser.getAttributeValue(null, "xadvance");
                parser.getAttributeValue(null, "page");
                parser.getAttributeValue(null, "chnl");
                character = parser.getAttributeValue(null, "letter").charAt(0);

                entries_counter++;
                FontData nfd = new FontData(ID, character, y, x, width, height);
                entries.add(nfd);

                System.out.println(nfd);
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, ns, "char");
            }

        }
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "chars");
        return entries;

    }




}
