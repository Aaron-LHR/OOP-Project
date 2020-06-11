package src.UI;

import com.vdurmont.emoji.EmojiParser;

public class emoji {
    public static void main(String[] args) {
        String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);
        System.out.println(result);
    }
}
