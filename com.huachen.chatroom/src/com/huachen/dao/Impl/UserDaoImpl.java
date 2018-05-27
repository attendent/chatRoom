package com.huachen.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.huachen.dao.UserDao;
import com.huachen.model.ChatRoom;
import com.huachen.model.User;
import com.huachen.util.JdbcUtils;

public class UserDaoImpl implements UserDao {

	JdbcUtils util = new JdbcUtils();

	@Override
	public boolean isExist(String userName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from user where username=?";
		JdbcUtils util = new JdbcUtils();
		try {
			con = JdbcUtils.getCon();
			ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean isExist(String userName, String password) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from user where username=? and password=? ";
		JdbcUtils util = new JdbcUtils();
		try {
			con = JdbcUtils.getCon();
			ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	// 注册
	@Override
	public boolean register(User user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "insert into user (username,password,nickname,mail) values(?,?,?,?);";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getNickName());
			ps.setString(4, user.getMail());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	// 修改
	@Override
	public boolean update(User user, String nextpassword) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "update user set password='" + nextpassword + "' where username='" + user.getUserName() + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	// 得到所有用户
	@Override
	public List<User> getListAll() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<User> list = new ArrayList<User>();// 创建list集合，用于保持User对象
		try {
			con = JdbcUtils.getCon();
			String sql = "select * from user";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();// 创建User对象用于保持从数据看查出来的数据
				user.setId(rs.getInt(1));
				user.setUserName(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setNickName(rs.getString(4));
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return list;
	}

	// 找到用戶
	@Override
	public User find(Integer id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = new User();
		try {
			con = JdbcUtils.getCon();
			String sql = "SELECT * FROM user WHERE id = '" + id + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				user.setId(rs.getInt(1));
				user.setUserName(rs.getString(2));
				user.setPassword(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return user;
	}

	@Override
	public User find(String userName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = new User();
		try {
			con = JdbcUtils.getCon();
			String sql = "SELECT * FROM user WHERE username = '" + userName + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				user.setId(rs.getInt(1));
				user.setUserName(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setNickName(rs.getString(4));
				user.setMail(rs.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return user;
	}

	// 得到所有朋友
	@Override
	public List<User> getFriends(Integer userId) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		List<User> list = new ArrayList<>();
		try {
			con = JdbcUtils.getCon();
			String sql = "SELECT user.* FROM user,user_friend uf WHERE uf.userid = '" + userId
					+ "' AND user.id = uf.friendid AND uf.status=1";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setUserName(rs.getString(2));
				user.setNickName(rs.getString(4));
				sql = "SELECT * FROM user_friend uf WHERE userid = '" + userId + "' AND friendid = '"
						+ rs.getInt(1) +"'";
				ps2 = con.prepareStatement(sql);
				rs2 = ps2.executeQuery();
				while(rs2.next()) {
					user.setReMark(rs2.getString(4));
				}
				ps2.close();
				rs2.close();
				list.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return list;
	}

	@Override
	public boolean addFriend(Integer userId, Integer friendId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "INSERT INTO user_friend (userid,friendid,status) VALUES(?,?,?)";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, friendId);
			ps.setInt(3, 0);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean isExist(Integer userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from user where id='" + userId + "'";
		JdbcUtils util = new JdbcUtils();
		try {
			con = JdbcUtils.getCon();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean delFriend(Integer userId, Integer friendId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "DELETE FROM user_friend WHERE (userid = '" + userId + "' AND friendId = '" + friendId
					+ "') OR (userid = '" + friendId + "' AND userid = '" + friendId + "')";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean isFriend(Integer userId, Integer friendId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "SELECT * FROM user_friend WHERE userid = '" + userId + "' ANd friendid = '" + friendId
					+ "'AND status = 1";
			ps = (PreparedStatement) con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public List<ChatRoom> getChatRooms(Integer userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ChatRoom> chatRooms = new ArrayList<>();// 创建list集合，用于保持User对象
		try {
			con = JdbcUtils.getCon();
			String sql = "SELECT c.* FROM chatroom c,user_room uc WHERE uc.userid = '" + userId
					+ "' AND c.id = uc.roomid";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ChatRoom chatRoom = new ChatRoom();
				chatRoom.setId(rs.getInt(1));
				chatRoom.setRoomName(rs.getString(2));
				chatRooms.add(chatRoom);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return chatRooms;
	}

	@Override
	public boolean updateRemark(Integer userId, Integer friendId, String remark) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();

			String sql = "DELETE FROM user_friend WHERE userid = '" + userId + "' AND friendid = '" + friendId + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();

			sql = "INSERT INTO user_friend (userid,friendid,status,remark) VALUES(?,?,?,?)";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, friendId);
			ps.setInt(3, 1);
			ps.setString(4, remark);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean addUserRoom(Integer userId, Integer roomId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "INSERT INTO user_room (userid,roomid) VALUES(?,?)";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, roomId);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean isUserRoom(Integer userId, Integer roomId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "SELECT * FROM user_room WHERE userid = '" + userId + "' ANd roomid = '" + roomId + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public String selectMail(String userName) {
		String userMail = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "select * from user where username = '" + userName + "'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				userMail = rs.getString("mail");
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return userMail;
	}

	@Override
	public boolean quitUserRoom(Integer userId, Integer roomId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "DELETE FROM user_room WHERE userid = '" + userId + "' AND roomid = '" + roomId + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean changeNickName(Integer userId, String nextNickName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "update user set nickname='" + nextNickName + "' where id='" + userId + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public List<User> getApplicants(Integer userId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<User> applicants = new ArrayList<>();
		try {
			con = JdbcUtils.getCon();
			String sql = "select * from user,user_friend uf where friendid='" + userId
					+ "' AND user.id = uf.userid AND uf.status = 0";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setNickName(rs.getString(4));
				applicants.add(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return applicants;
	}

	@Override
	public boolean agreedFriend(Integer userId, Integer applicantId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "UPDATE user_friend SET status = 1 where friendid ='" + userId + "' AND userid = '"
					+ applicantId + "'";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();

			sql = "INSERT INTO user_friend(userid,friendid,status) value(?,?,?)";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, applicantId);
			ps.setInt(3, 1);
			ps.executeUpdate();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean refuseFriend(Integer userId, Integer applicantId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JdbcUtils.getCon();
			String sql = "DELETE FROM user_friend WHERE userid = '" + applicantId + "' AND friendid = '" + userId
					+ "' AND status = 0";
			ps = (PreparedStatement) con.prepareStatement(sql);
			ps.executeUpdate();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

	@Override
	public boolean isApplicant(Integer userId, Integer applicantId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM user_friend WHERE (userid = '" + userId + "' AND friendid = '" + applicantId
				+ "' AND status = 0) OR (userid = '" + applicantId + "' AND friendid = '" + userId
				+ "' AND status = 0)";
		JdbcUtils util = new JdbcUtils();
		try {
			con = JdbcUtils.getCon();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.close(ps, con, rs);
		}
		return false;
	}

}
