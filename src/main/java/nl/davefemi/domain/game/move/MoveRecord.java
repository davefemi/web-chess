package nl.davefemi.domain.game.move;

import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;

public record MoveRecord (Move move, PlayerColor color, PieceType movedPiece, PieceType capturedPiece, boolean upForPromotion){


}
