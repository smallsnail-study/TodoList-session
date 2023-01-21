package org.zerock.w2.dao;

import lombok.Cleanup;
import org.zerock.w2.domain.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {    // 자바에서 데이터베이스의 데이터를 객체로 처리

    public MemberVO getWithPassword(String mid, String mpw) throws Exception {

        String query = "select mid, mpw, mname from tbl_member where mid =? and mpw = ?";

        MemberVO memberVO = null;

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, mid);
        preparedStatement.setString(2, mpw);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        memberVO = MemberVO.builder()
                .mid(resultSet.getString(1))
                .mpw(resultSet.getString(2))
                .mname(resultSet.getString(3))
                .build();

        return memberVO;
    }

    // rememberMe가t true라면 tbl_member 테이블 사용자의 정보에 uuid를 수정하는 기능
    public void updateUuid(String mid, String uuid) throws Exception {

        String sql = "update tbl_member set uuid =? where mid = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, mid);
        preparedStatement.executeUpdate();
    }
}
