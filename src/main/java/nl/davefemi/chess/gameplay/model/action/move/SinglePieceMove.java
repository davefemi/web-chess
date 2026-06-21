package nl.davefemi.chess.gameplay.model.action.move;

import nl.davefemi.chess.gameplay.model.board.Square;

public sealed interface SinglePieceMove extends Move permits SingleMove, EnPassantMove, PromotionMove {

    SingleMove move();

    default Square from(){
        return move().from();
    }

    default Square to(){
        return move().to();
    }

}
