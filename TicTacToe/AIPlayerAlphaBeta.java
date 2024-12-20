package TicTacToe;

import java.util.*;

/** AIPlayer menggunakan algoritma Minimax dengan Pemangkasan Alpha-Beta */
public class AIPlayerAlphaBeta extends AIPlayer {

    private static final int[] winningPatterns = {
        0b111000000, 0b000111000, 0b000000111, // 3 baris
        0b100100100, 0b010010010, 0b001001001, // 3 kolom
        0b100010001, 0b001010100 // 2 diagonal
    };

    /** Konstruktor dengan papan permainan yang diberikan */
    public AIPlayerAlphaBeta(Board board) {
        super(board);
    }

    /** Dapatkan langkah terbaik berikutnya untuk komputer. Kembalikan int[2] dari {row, col} */
    @Override
    public int[] move() {
        int[] result = minimax(2, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{result[1], result[2]}; // row, col
    }

    /** Minimax (rekursif) pada level kedalaman untuk pemain yang memaksimalkan atau meminimalkan
     * dengan pemangkasan alpha-beta. Kembalikan int[3] dari {score, row, col} */
    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        // Menghasilkan langkah berikutnya yang mungkin dalam daftar int[2] dari {row, col}.
        List<int[]> nextMoves = generateMoves();

        // mySeed adalah memaksimalkan; sementara oppSeed adalah meminimalkan
        int score;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            // Gameover atau kedalaman tercapai, evaluasi skor
            score = evaluate();
            return new int[]{score, bestRow, bestCol};
        } else {
            for (int[] move : nextMoves) {
                // Coba langkah ini untuk "pemain" saat ini
                cells[move[0]][move[1]].content = player;

                if (player == mySeed) { // mySeed (komputer) adalah pemain yang memaksimalkan
                    score = minimax(depth - 1, oppSeed, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else { // oppSeed adalah pemain yang meminimalkan
                    score = minimax(depth - 1, mySeed, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }

                // Batalkan langkah
                cells[move[0]][move[1]].content = Seed.EMPTY;

                // Cut-off
                if (alpha >= beta) break;
            }
            return new int[]{(player == mySeed) ? alpha : beta, bestRow, bestCol};
        }
    }

    /** Temukan semua langkah berikutnya yang valid.
     * Kembalikan Daftar langkah dalam int[2] dari {row, col} atau daftar kosong jika gameover */
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>(); // alokasikan Daftar

        // Jika gameover, yaitu tidak ada langkah berikutnya
        if (hasWon(mySeed) || hasWon(oppSeed)) {
            return nextMoves; // kembalikan daftar kosong
        }

        // Cari sel kosong dan tambahkan ke Daftar
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.EMPTY) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }

    /** Fungsi evaluasi heuristik untuk papan saat ini
     * @Kembalikan +100, +10, +1 untuk MASING-MASING 3-, 2-, 1-dalam-baris untuk komputer.
     *         -100, -10, -1 untuk MASING-MASING 3-, 2-, 1-dalam-baris untuk lawan.
     *         0 jika tidak */
    private int evaluate() {
        int score = 0;
        // Evaluasi skor untuk masing-masing dari 8 baris (3 baris, 3 kolom, 2 diagonal)
        score += evaluateLine(0, 0, 0, 1, 0, 2); // baris 0
        score += evaluateLine(1, 0, 1, 1, 1, 2); // baris 1
        score += evaluateLine(2, 0, 2, 1, 2, 2); // baris 2
        score += evaluateLine(0, 0, 1, 0, 2, 0); // kolom 0
        score += evaluateLine(0, 1, 1, 1, 2, 1); // kolom 1
        score += evaluateLine(0, 2, 1, 2, 2, 2); // kolom 2
        score += evaluateLine(0, 0, 1, 1, 2, 2); // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0); // diagonal alternatif
        return score;
    }

    /** Fungsi evaluasi heuristik untuk baris 3 sel yang diberikan
     * @Kembalikan +100, +10, +1 untuk 3-, 2-, 1-dalam-baris untuk komputer.
     *         -100, -10, -1 untuk 3-, 2-, 1-dalam-baris untuk lawan.
     *         0 jika tidak */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // Sel pertama
        if (cells[row1][col1].content == mySeed) {
            score = 1;
        } else if (cells[row1][col1].content == oppSeed) {
            score = -1;
        }

        // Sel kedua
        if (cells[row2][col2].content == mySeed) {
            if (score == 1) { // sel1 adalah mySeed
                score = 10;
            } else if (score == -1) { // sel1 adalah oppSeed
                return 0;
            } else { // sel1 kosong
                score = 1;
            }
        } else if (cells[row2][col2].content == oppSeed) {
            if (score == -1) { // sel1 adalah oppSeed
                score = -10;
            } else if (score == 1) { // sel1 adalah mySeed
                return 0;
            } else { // sel1 kosong
                score = -1;
            }
        }

        // Sel ketiga
        if (cells[row3][col3].content == mySeed) {
            if (score > 0) { // sel1 dan/atau sel2 adalah mySeed
                score *= 10;
            } else if (score < 0) { // sel1 dan/atau sel2 adalah oppSeed
                return 0;
            } else { // sel1 dan sel2 kosong
                score = 1;
            }
        } else if (cells[row3][col3].content == oppSeed) {
            if (score < 0) { // sel1 dan/atau sel2 adalah oppSeed
                score *= 10;
            } else if (score > 0) { // sel1 dan/atau sel2 adalah mySeed
                return 0;
            } else { // sel1 dan sel2 kosong
                score = -1;
            }
        }
        return score;
    }

    /** Mengembalikan true jika thePlayer menang */
    private boolean hasWon(Seed thePlayer) {
        int pattern = 0b000000000; // pola 9-bit untuk 9 sel
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == thePlayer) {
                    pattern |= (1 << (row * COLS + col));
                }
            }
        }
        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) return true;
        }
        return false;
    }
}
