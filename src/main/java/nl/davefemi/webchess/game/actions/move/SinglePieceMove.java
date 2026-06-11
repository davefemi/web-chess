package nl.davefemi.webchess.game.actions.move;

import nl.davefemi.webchess.game.board.Square;

public sealed interface SinglePieceMove extends Move permits SingleMove, EnPassantMove, PromotionMove {

    SingleMove move();

    default Square from(){
        return move().from();
    }

    default Square to(){
        return move().to();
    }

}
