package me.herobrinedobem.heventos.databases;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.entity.Player;
import me.herobrinedobem.heventos.HEventos;

public class SQLite {

	private String user, password, database, host;
	private Connection connection;
	private Statement stmt;

	public SQLite() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + HEventos.getHEventos().getDataFolder().getAbsolutePath() + File.separator + "database.db");
			this.stmt = this.connection.createStatement();
			this.stmt.execute("CREATE TABLE IF NOT EXISTS eventos (player VARCHAR(255), wins INTEGER, participations INTEGER)");
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addNew(final String player, final int wins, final int participations) {
		try {
			Class.forName("org.sqlite.JDBC");
			final String sql = "INSERT INTO eventos (player, wins, participations) VALUES ('" + player + "', '" + wins + "', '" + participations + "');";
			this.stmt.executeUpdate(sql);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void updateWins(final String player, final int wins) {
		try {
			Class.forName("org.sqlite.JDBC");
			final String sql = "UPDATE eventos SET wins='" + (this.getWins(player) + wins) + "' WHERE player='" + player + "';";
			this.stmt.executeUpdate(sql);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void updateParticipations(final String player, final int participations) {
		try {
			Class.forName("org.sqlite.JDBC");
			final String sql = "UPDATE eventos SET participations='" + (this.getParticipations(player) + participations) + "' WHERE player='" + player + "';";
			this.stmt.executeUpdate(sql);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public int getWins(final String player) {
		try {
			Class.forName("org.sqlite.JDBC");
			final String sql = "SELECT wins FROM eventos WHERE player='" + player + "';";
			final ResultSet rs = this.stmt.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt("wins");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getParticipations(final String player) {
		try {
			Class.forName("org.sqlite.JDBC");
			final String sql = "SELECT participations FROM eventos WHERE player='" + player + "';";
			final ResultSet rs = this.stmt.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt("participations");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean hasPlayer(final String player) {
		try {
			Class.forName("org.sqlite.JDBC");
			final String sql = "SELECT * FROM eventos WHERE player='" + player + "'";
			final ResultSet rs = this.stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("player").equalsIgnoreCase(player)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void getTOPWins(final Player p) {
		try {
			p.sendMessage(HEventos.getHEventos().getConfigUtil().getTopVencedores());
			Class.forName("org.sqlite.JDBC");
			final String sql = "SELECT * FROM eventos ORDER BY wins DESC LIMIT 10";
			final ResultSet rs = this.stmt.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				i++;
				p.sendMessage(HEventos.getHEventos().getConfigUtil().getTopVencedoresPosicao().replace("$posicao$", i + "").replace("$player$", rs.getString("player")).replace("$vitorias$", rs.getInt("wins") + ""));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void getTOPParticipations(final Player p) {
		try {
			p.sendMessage(HEventos.getHEventos().getConfigUtil().getTopParticipacoes());
			Class.forName("org.sqlite.JDBC");
			final String sql = "SELECT * FROM eventos ORDER BY participations DESC LIMIT 10";
			final ResultSet rs = this.stmt.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				i++;
				p.sendMessage(HEventos.getHEventos().getConfigUtil().getTopParticipacoesPosicao().replace("$posicao$", i + "").replace("$player$", rs.getString("player")).replace("$participacoes$", rs.getInt("wins") + ""));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void addPartipationPoint(final String player) {
		if (this.hasPlayer(player)) {
			this.updateParticipations(player, 1);
		} else {
			this.addNew(player, 0, 1);
		}
	}

	public void addWinnerPoint(final String player) {
		if (this.hasPlayer(player)) {
			HEventos.getHEventos().getSqlite().updateWins(player, 1);
		} else {
			HEventos.getHEventos().getSqlite().addNew(player, 1, 1);
		}
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(final String database) {
		this.database = database;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void setConnection(final Connection connection) {
		this.connection = connection;
	}

	public Statement getStmt() {
		return this.stmt;
	}

	public void setStmt(final Statement stmt) {
		this.stmt = stmt;
	}

}
