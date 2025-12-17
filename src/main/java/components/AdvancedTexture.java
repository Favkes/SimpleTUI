package components;

import java.util.ArrayList;
import java.util.function.Function;

public class AdvancedTexture extends Texture {
    // Function defining how each row should be shifted in the texture (for more advanced behaviour)
    Function<Integer, Integer> shiftPerRow;

    public AdvancedTexture(String patternSeed) {
        this(patternSeed, 1, p -> 0);
    }

    public AdvancedTexture(String patternSeed, int repeatTimes) {
        this(patternSeed, repeatTimes, p -> 0);
    }

    public AdvancedTexture(String patternSeed, int repeatTimes, Function<Integer, Integer> shiftPerRow) {
        super(patternSeed, repeatTimes);
        this.shiftPerRow = shiftPerRow;
    }

    public ArrayList<Pixel> generateRepeatingSubarray(int from, int to, int row) {
        return super.generateRepeatingSubarray(
                from + shiftPerRow.apply(row),
                to + shiftPerRow.apply(row)
        );
    }
}
