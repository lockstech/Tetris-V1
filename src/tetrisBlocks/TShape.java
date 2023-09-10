package tetrisBlocks;

import tetris.TetrisBlock;

public class TShape extends TetrisBlock {
    public TShape(){
        super(new int[][]{{1, 1, 1},
                          {0, 1, 0}}); // super для доступа к private.
    }
}
