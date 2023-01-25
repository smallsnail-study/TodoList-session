package org.zerock.w2.dao;

import lombok.Cleanup;
import org.zerock.w2.domain.TodoVO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    public String getTime() {

        String now = null;

        try (Connection connection = ConnectionUtil.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select now()");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            resultSet.next();

            now = resultSet.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    public String getTime2() throws Exception {
        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        String now = resultSet.getString(1);

        return now;
    }

    // 등록 기능 구현
    public void insert(TodoVO vo) throws Exception {
        String sql = "insert into tbl_todo (title, dueDate, finished) values(?,?,?)";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());

        preparedStatement.executeUpdate();
    }

    // tbl_todo 내의 모든 데이터를 가져오는 목록 기능 구현
    public List<TodoVO> selectAll() throws Exception {

        String sql = "select * from tbl_todo";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 쿼리(select)를 실행해야 하기 때문에 PreparedStatement의 executeQuery()를 이용해서 ResultSet을 구한다.
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        // 테이블의 각 행(row)은 하나의 TodoVO 객체가 될 것이고, 모든 TodoVO를 담을 수 있도록 List<TodoVO> 타입을 리턴타입으로 한다.
        List<TodoVO> list = new ArrayList<>();

        // ResultSet으로 각 행(row)을 이동하면서 각 행(row)의 데이터를 TodoVO로 변환한다.
        // next()의 결과는 이동할 수 있는 행(row)이 존재하는 경우 true, 아닌 경우 false
        // 각 행을 반복문을 통해 가져온다.
        while (resultSet.next()) {
            // TodoVO는 빌더 패턴을 이용해서 간편하게 TodoVO 객체를 생성한다.
            // tno/title등의 속성값을 ResultSet에서 가져온 데이터로 처리한다.
            // ResultSet의 get()는 칼럼의 인덱스 번호를 이용하거나 칼럼의 이름을 지정해서 가져올 수 있다.
            TodoVO vo = TodoVO.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();

            list.add(vo);
        }
        return list;
    }

    // 특정번호(tno)의 데이터만 가져오는 조회 기능 구현
    public TodoVO selectOne(Long tno) throws Exception {

        String sql = "select * from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);

        // 쿼리(select)를 실행해야 하기 때문에 PreparedStatement의 executeQuery()를 이용해서 ResultSet을 구한다.
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        // 여러개의 데이터가 나오는 목록구현과 달리 한 행(row)의 데이터만 나오기 때문에 반복문 없이 한번만 resultSet.next()를 실행하면 된다.
        resultSet.next();
        TodoVO vo = TodoVO.builder()
                .tno(resultSet.getLong("tno"))
                .title(resultSet.getString("title"))
                .dueDate(resultSet.getDate("dueDate").toLocalDate())
                .finished(resultSet.getBoolean("finished"))
                .build();

        return vo;
    }

    // 특정번호(tno)의 데이터 삭제 기능 구현
    public void deleteOne(Long tno) throws Exception {

        String sql = "delete from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);
        preparedStatement.executeUpdate();
    }

    // 특정번호(tno)의 데이터 수정 기능 구현
    public void updateOne(TodoVO todoVO) throws Exception{

        String sql = "update tbl_todo set title = ?, dueDate = ?, finished = ? where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, todoVO.getTitle());
        preparedStatement.setDate(2, Date.valueOf(todoVO.getDueDate()));
        preparedStatement.setBoolean(3, todoVO.isFinished());
        preparedStatement.setLong(4, todoVO.getTno());

        preparedStatement.executeUpdate();
    }
}
