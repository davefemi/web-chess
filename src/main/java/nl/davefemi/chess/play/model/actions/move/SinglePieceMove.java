package nl.davefemi.chess.play.model.actions.move;

import nl.davefemi.chess.play.model.board.Square;

public sealed interface SinglePieceMove extends Move permits SingleMove, EnPassantMove, PromotionMove {

    SingleMove move();

    default Square from(){
        return move().from();
    }

    default Square to(){
        return move().to();
    }

}
