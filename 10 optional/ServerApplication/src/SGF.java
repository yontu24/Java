import com.jcraft.jsch.*;

public class SGF {
    private StringBuilder format = new StringBuilder();
    private static JSch jsch = new JSch();
    private static final String USER = "root";
    private static final String HOST = "127.0.0.1";
    private static final String PASS = "mypass";
    private Session session;

    public SGF() throws JSchException {
        session = jsch.getSession(USER, HOST);
        session.setPassword(PASS);
        session.connect();
        System.out.println("Connection established.");
    }

    public void uploadFile() throws JSchException, SftpException {
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        System.out.println("SFTP Channel created.");

        sftpChannel.put("C:\\Users\\User\\Desktop\\FACULTATE\\AP\\10\\ServerApplication\\sgf.txt", "localhost\\sgf.txt");

        sftpChannel.disconnect();
        session.disconnect();

        System.out.println("File upload succesfully!");
    }

    public void addFormat(String rule, String comment, boolean addNextLine) {
        format.append(rule);
        format.append("[");
        format.append(comment);
        format.append("]");

        if (addNextLine)
            format.append("\n");
    }

    public String getFormat() {
        return format.toString();
    }
}
