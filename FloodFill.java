import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;



class MinhaFila<T> {
    private T[] fila;
    private int tamanho;
    private int frente;
    private int fim;

    @SuppressWarnings("unchecked")
    public MinhaFila(int capacidade) {
        fila = (T[]) new Object[capacidade];
        tamanho = 0;
        frente = 0;
        fim = -1;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public boolean isFull() {
        return tamanho == fila.length;
    }

    public void add(T item) {
        if (isFull()) {
            throw new IllegalStateException("Fila cheia");
        }
        fim = (fim + 1) % fila.length;
        fila[fim] = item;
        tamanho++;
    }

    public T remove() {
        if (isEmpty()) {
            throw new IllegalStateException("Fila vazia");
        }
        T item = fila[frente];
        fila[frente] = null;
        frente = (frente + 1) % fila.length;
        tamanho--;
        return item;
    }
}



public class FloodFill {
    private BufferedImage fotadas;

    public FloodFill() {
        JFileChooser escolherAquivo = new JFileChooser();

        if (escolherAquivo.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File arquivoEscolhido = escolherAquivo.getSelectedFile();
                fotadas = ImageIO.read(arquivoEscolhido);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Configurando a janela
        JFrame janeladas = new JFrame("Fotão do pai ixiii");
        janeladas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janeladas.setSize(fotadas.getWidth(), fotadas.getHeight());

        JPanel janela = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fotadas, 0, 0, this);
            }
        };

        MouseAdapter escutarMouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (x < fotadas.getWidth() && y < fotadas.getHeight()) {
                    Color corPixelClicado = new Color(fotadas.getRGB(x, y));
                    new Thread(() -> {
                        floodFill(x, y, corPixelClicado, Color.RED);
                    }).start();
                }
            }

            private void floodFill(int startX, int startY, Color corOriginal, Color novaCor) {
                if (startX < 0 || startX >= fotadas.getWidth() || startY < 0 || startY >= fotadas.getHeight()) {
                    return;
                }


                if (corOriginal.equals(novaCor)) {
                    return;
                }



                MinhaFila<Point> fila = new MinhaFila<>(fotadas.getWidth() * fotadas.getHeight());
                fila.add(new Point(startX, startY));

                while (!fila.isEmpty()) {
                    Point p = fila.remove();
                    int x = p.x;
                    int y = p.y;




                    if (x < 0 || x >= fotadas.getWidth() || y < 0 || y >= fotadas.getHeight()) {
                        continue;
                    }



                    Color corAtual = new Color(fotadas.getRGB(x, y));
                    if (!corAtual.equals(corOriginal)) {
                        continue;
                    }



                    fotadas.setRGB(x, y, novaCor.getRGB());



                    fila.add(new Point(x + 1, y));
                    fila.add(new Point(x - 1, y));
                    fila.add(new Point(x, y + 1));
                    fila.add(new Point(x, y - 1));



                    janela.repaint();


                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        janela.addMouseListener(escutarMouse);
        janeladas.add(janela);
        janeladas.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FloodFill::new);
    }
}
