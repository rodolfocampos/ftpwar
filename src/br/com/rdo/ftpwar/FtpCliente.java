package br.com.rdo.ftpwar;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpCliente {

    private String server = "ftp.com.br";
    private String username = "user";
    private String password = "senha";


    public void conectar() {
        FTPClient ftp = new FTPClient();

        boolean error = false;

        try {
            int reply;
            ftp.connect(server);
            ftp.login(username, password);
            System.out.println("Conectando a " + server + ".");
            System.out.print(ftp.getReplyString());

            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("Conexão recusada pelo servidor FTP");
                System.exit(1);
            }

            ftp.setControlKeepAliveTimeout(300);
            //vai pro diretório webapps
            ftp.changeWorkingDirectory("/caminho/webapps");
            //deleta arquivos
            ftp.deleteFile("ROOT.war");
            ftp.removeDirectory("ROOT");
            // transfere arquivo
            File arquivo = new File("/home/caminho/arquivo.war");
            enviarArquivoe(arquivo, ftp);

            ftp.disconnect();

        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
            System.exit(error ? 1 : 0);
        }

    }

    public void enviarArquivoe(File arquivo, FTPClient ftp) {

        FileInputStream is;

        try {
            is = new FileInputStream(arquivo);
            if (ftp.storeFile("ROOT.war", is)) {
                System.out.println("Arquivo enviado com sucesso");
            } else {
                System.out.println("Erro ao enviar arquivo");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
