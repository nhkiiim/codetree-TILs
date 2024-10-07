import java.util.*;
import java.io.*;

public class Main {

    static int R;
    static int C;
    static int K;
    static int[][] map;
    static int[] moveX;
    static int[] moveY;
    static boolean[][] visited;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken()); // 1 ~ R
        C = Integer.parseInt(st.nextToken()); // 1 ~ C
        K = Integer.parseInt(st.nextToken());

        moveX = new int[] {-1, 0, 1, 0};
        moveY = new int[] {0, 1, 0, -1};

        // 0 1 2 3 
        // 북 동 남 서

        // 요정 1, 골렘 i.., 출구 -1
        map = new int[R+4][C+2];
        visited = new boolean[R+4][C+2];

        int answer = 0;

        //이동 : 남 -> 서(반시계방향회전) -> 동(시계방향회전)
        for (int i=1; i<=K; i++) {
            st = new StringTokenizer(br.readLine());
            
            int ci = Integer.parseInt(st.nextToken());
            int di = Integer.parseInt(st.nextToken());

            int yoX = 1;
            int yoY = ci;
            
            int golExit = di;

            for (;;) { 
                
                int tempYoX = yoX + 1;
                int tempYoY = yoY;

                boolean okFlag = checkIfOk(tempYoX, tempYoY);

                if (okFlag) {
                    yoX++;
                    continue;
                }

                tempYoX = yoX + 1;
                tempYoY = yoY - 1;

                okFlag = checkIfOk(tempYoX, tempYoY);

                if (okFlag) {
                    yoX++;
                    yoY--;

                    golExit = golExitCalc(golExit, -1);
                    continue;
                }

                tempYoX = yoX + 1;
                tempYoY = yoY + 1;

                okFlag = checkIfOk(tempYoX, tempYoY);

                if (okFlag) {
                    yoX++;
                    yoY++;

                    golExit = golExitCalc(golExit, 1);
                    continue;
                }

                if (yoX < 3) {
                    for (int k=0; k<R+4; k++) {
                        for (int j=0; j<C+2; j++) {
                            map[k][j] = 0;
                        }
                    }

                    break;
                }

                map[yoX][yoY] = i;

                for (int k=0; k<4; k++) {
                    int golX = yoX + moveX[k];
                    int golY = yoY + moveY[k];
                    map[golX][golY] = i;
                }

                map[yoX + moveX[golExit]][yoY + moveY[golExit]] = 1000+i;
                
                int maxR = bfs(yoX, yoY, i);
                answer+= maxR - 2;
                
                // System.out.println(i+" 번째 " + (maxR-2)+ " -> 누적 : " + answer);
                break;
            }
            
        }

        System.out.println(answer);
    }

    public static boolean checkIfOk(int tempYoX, int tempYoY) {
        for (int i=0; i<4; i++) {
            int golX = tempYoX + moveX[i];
            int golY = tempYoY + moveY[i];

            if (map[golX][golY] != 0 || golX > R+2 || golY > C || golY < 1) {
                return false;
            }
        }

        return true;
    }

    public static int golExitCalc(int golExit, int calc) {
        golExit += calc;

        if (golExit == 4) {
            golExit = 0;
        
        } else if (golExit == -1) {
            golExit = 3;
        }

        return golExit;
    }

    public static int bfs(int x, int y, int i) {
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{x, y, i});
        visited[x][y] = true;

        int max = x;

        while (!q.isEmpty()) {
            int[] arr = q.poll();

            if (arr[0] > max) {
                max = arr[0];
            }
             
            for (int k=0; k<4; k++) {
                int goX = arr[0] + moveX[k];
                int goY = arr[1] + moveY[k];

                if (goX > (R+2) || goX < 3 || goY > C || goY < 1 || map[goX][goY] == 0) {
                    continue;
                }   

                if ((arr[2] > 1000) && !visited[goX][goY]) {
                    q.offer(new int[]{goX, goY, map[goX][goY]});
                    visited[goX][goY] = true;

                } else if ((map[goX][goY] == arr[2] || (map[goX][goY]-1000) == arr[2]) && !visited[goX][goY]) {
                    q.offer(new int[]{goX, goY, map[goX][goY]});
                    visited[goX][goY] = true;
                }
                
            }
        }

        for (int k=0; k<R+4; k++) {
            for (int h=0; h<C+2; h++) {
                visited[k][h] = false;
            }
        }

        return max;
    }
}