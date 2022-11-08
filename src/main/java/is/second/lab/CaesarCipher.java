package is.second.lab;

import org.apache.commons.io.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class CaesarCipher {
    private static final int alphabetSize = 32;
    private static final int firstPositionOfChars = 1040;
    private static final StringBuilder alphabet = new StringBuilder("");
    private static final String INPUT_FILE_PATH = "src/main/resources/InputData.txt";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/Result.txt";


    public static void main(String[] args){
        try (
            FileInputStream inputStream = new FileInputStream(INPUT_FILE_PATH);
            FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE_PATH);
            Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println("Введите ключевое слово: ");
            String secretKey = removeDuplicates(scanner.nextLine().trim()).toUpperCase();
            outputStream.write(("Ключевое слово: " + secretKey + "\n").getBytes(StandardCharsets.UTF_8));

            System.out.println("Введите ключ кодирования 1-32: ");
            int offset = scanner.nextInt() % alphabetSize;
            outputStream.write(("Ключ кодирования: " + offset + "\n").getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Получившийся алфавит: " + createAlphabet(secretKey, offset) + "\n").getBytes(StandardCharsets.UTF_8));

            String rawData = prepareText(IOUtils.toString(inputStream, StandardCharsets.UTF_8).trim().toUpperCase());
            outputStream.write(("Исходный текст: " + rawData + "\n").getBytes(StandardCharsets.UTF_8));
            String encodedData = cipher(rawData, offset);
            outputStream.write(("Зашифрованный текст: " + encodedData + "\n").getBytes(StandardCharsets.UTF_8));
            outputStream.write(("Расшифрованный текст: " + decipher(encodedData, offset) + "\n").getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            System.err.println("SOMETHING WENT WRONG");
        }
    }

    public static String createAlphabet(String secretKey, int offset){
        StringBuilder tmpAlphabet = new StringBuilder("");
        for(int i = 0; i < alphabetSize; i++){
            tmpAlphabet.append(String.valueOf(
                    ((char) ((firstPositionOfChars + i + offset)
                            % (firstPositionOfChars + alphabetSize + offset))
                    )).toUpperCase());
        }
        return alphabet.append(removeDuplicates(insertString(tmpAlphabet.toString(), secretKey, offset))).toString();
    }

    public static String insertString(String originalString, String stringToBeInserted, int index) {
        return originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1);
    }

    public static String prepareText(String str){
        return str.trim().replaceAll("[^А-Яа-я ]", "").toUpperCase();
    }

    public static String removeDuplicates(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAtPosition = str.charAt(i);
            if (result.toString().indexOf(charAtPosition) < 0)
                result.append(charAtPosition);
        }
        return result.toString();
    }

    public static String cipher(String data, int offset) {
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            if (character != ' ') {
                int originalAlphabetPosition = alphabet.indexOf(String.valueOf(character));
                int newAlphabetPosition = (originalAlphabetPosition + offset) % alphabetSize;
                result.append(alphabet.charAt(newAlphabetPosition));
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public static String decipher(String data, int offset) {
        return cipher(data, alphabetSize - (offset % alphabetSize));
    }
}