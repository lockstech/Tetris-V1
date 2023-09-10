package tetrisBlocks;

import tetris.TetrisBlock;

public class IShape extends TetrisBlock {
    public IShape(){
        super(new int[][]{{1,1,1,1}}); // super для доступа к private.
    }
    @Override
    public void rotate(){ // Поворот для I другой, из-за особенностей фигуры.
        super.rotate(); // Сохранение функционала оригинального метода.
    }
}
