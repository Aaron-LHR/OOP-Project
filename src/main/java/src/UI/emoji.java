package src.UI;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class emoji {

    public static String[] getEmojiUnicode() {
        List<String> emojiList = new ArrayList<>();

        for (Emoji emoji : EmojiManager.getAll()) {
            emojiList.add(emoji.getUnicode());
        }

        return emojiList.toArray(new String[emojiList.size()]);
    }


    public static void main(String[] args) {
        /*
        String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);
        System.out.println();
        for (String s : EmojiManager.getAllTags()) {
            System.out.println(EmojiParser.parseToUnicode(":" + s + ":"));
        }

         */
//        for (Emoji emoji : EmojiManager.getAll()) {
//            System.out.println(emoji.getUnicode());
//        }

        System.out.println(Pattern.matches("!!\\((.*?)\\)!!", "!!(0.gif)!!"));
    }

}
