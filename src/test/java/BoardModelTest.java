import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.qainfotech.tap.training.snl.api.Board;
import com.qainfotech.tap.training.snl.api.BoardModel;

import static org.assertj.core.api.Assertions.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class BoardModelTest {
	
	@Test 
	public void init_test() throws FileNotFoundException, UnsupportedEncodingException, IOException
	{
		BoardModel boardModel=new BoardModel();
		
		Board board=new Board();
		UUID uuid=UUID.randomUUID();
		boardModel.init(uuid);
		JSONObject testDataFile = new JSONObject( new String(Files.readAllBytes(Paths.get(uuid.toString() + ".board"))));
		
		
		
		int sizeOfBoard=board.getData().getJSONArray("steps").length();
		assertThat(sizeOfBoard).isEqualTo(101);
		
		assertThat(testDataFile.getJSONArray("players")).isNotEqualTo(null);
		assertThat(testDataFile.get("turn")).isNotEqualTo(null);
		assertThat(testDataFile.getJSONArray("steps")).isNotEqualTo(null);
	
	
	}
	
	@Test
	
	public void save_test() throws FileNotFoundException, UnsupportedEncodingException, IOException
	{
		Board board=new Board();
		JSONObject testdata=new JSONObject();
		testdata.put("name", "shadab");
		testdata.put("uuid", UUID.randomUUID());
		testdata.put("position", 0);
		JSONObject data=board.getData();
		
		data.getJSONArray("players").put(testdata);
		
		BoardModel boardModel=new BoardModel();;
		boardModel.save(board.getUUID(), data);
	
		
		JSONArray playersArray=(JSONArray) data.get("players");
		
	assertThat(playersArray.length()).isNotEqualTo(0);
}
	
	@Test
	
	public void test_data() throws IOException
	{
		Board board=new Board();
		BoardModel boardModel=new BoardModel();
		assertThat(boardModel.data(board.getUUID())).isNotEqualTo(null);
	}
}
