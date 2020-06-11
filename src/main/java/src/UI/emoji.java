package src.UI;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;

public class emoji {
    public static void main(String[] args) {
//        String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
//        String result = EmojiParser.parseToUnicode(str);
//        System.out.println();
//        for (String s : EmojiManager.getAllTags()) {
//            System.out.println(EmojiParser.parseToUnicode(":" + s + ":"));
//        }

        for (Emoji emoji : EmojiManager.getAll()) {
            System.out.println(emoji.getUnicode());
        }


    }
}
