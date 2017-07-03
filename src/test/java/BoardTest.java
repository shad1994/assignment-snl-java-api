import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;

import com.qainfotech.tap.training.snl.api.Board;
import com.qainfotech.tap.training.snl.api.BoardModel;
import com.qainfotech.tap.training.snl.api.GameInProgressException;
import com.qainfotech.tap.training.snl.api.InvalidTurnException;
import com.qainfotech.tap.training.snl.api.MaxPlayersReachedExeption;
import com.qainfotech.tap.training.snl.api.NoUserWithSuchUUIDException;
import com.qainfotech.tap.training.snl.api.PlayerExistsException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BoardTest {

	@Test(priority = 1)

	public void boardTest() throws FileNotFoundException, UnsupportedEncodingException, IOException {

		Board board = new Board();

		assertThat(board.getUUID().toString().length()).isNotEqualTo(0);

	}

	@Test(priority = 2, expectedExceptions = MaxPlayersReachedExeption.class)
	public void registerPlayer_maxPlayerTest()
			throws IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		Board board = new Board();

		String name1 = "shadab";
		String name2 = "nishant";
		String name3 = "akshay";
		String name4 = "jaspal";
		String name5 = "shadab";

		board.registerPlayer(name1);
		board.registerPlayer(name2);
		board.registerPlayer(name3);
		board.registerPlayer(name4);
		board.registerPlayer(name5);

		assertThat(board.getData().getJSONArray("players").length()).isEqualTo(4);
		System.out.println(board.getData().getJSONArray("players").length());
	}

	@Test(priority = 3, expectedExceptions = PlayerExistsException.class)
	public void registerPlayer_PlayerExistsTest()
			throws IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {

		Board board = new Board();

		String name1 = "shadab";
		String name2 = "shadab";
		String name3 = "akshay";
		String name4 = "jaspal";

		board.registerPlayer(name1);
		board.registerPlayer(name2);
		board.registerPlayer(name3);
		board.registerPlayer(name4);

		assertThat(board.getData().getJSONArray("players").length()).isEqualTo(4);
		System.out.println(board.getData().getJSONArray("players").length());
	}

	@Test(priority = 4, expectedExceptions = NoUserWithSuchUUIDException.class)

	public void deletePlayerTest() throws NoUserWithSuchUUIDException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption {
		Board board = new Board();
		String name1 = "shadab";
		String name2 = "sdgd";
		String name3 = "akshay";
		String name4 = "jaspal";

		board.registerPlayer(name1);
		board.registerPlayer(name2);
		board.registerPlayer(name3);
		board.registerPlayer(name4);
		UUID uuid = null;

		JSONArray playerArray = new JSONArray();
		playerArray = board.getData().getJSONArray("players");

		for (int i = 0; i < playerArray.length(); i++) {
			JSONObject singlePlayer = playerArray.getJSONObject(i);
			if (singlePlayer.get("name").equals("shadab")) {

				uuid = (UUID) singlePlayer.get("uuid");
				System.out.println(uuid);
			}

		}
		System.out.println(playerArray.toString());
		board.deletePlayer(uuid);
		board.deletePlayer(UUID.randomUUID());
		assertThat(board.getData().length()).isEqualTo(3);

	}

	@Test

	public void test_rollDice_DoesPlayerMoveToNewPosition_Test()
			throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {

		Board board = new Board();
		String name1 = "shadab";
		String name2 = "sdgd";
		String name3 = "akshay";
		String name4 = "jaspal";

		board.registerPlayer(name1);
		board.registerPlayer(name2);
		board.registerPlayer(name3);
		board.registerPlayer(name4);

		JSONObject data = board.getData();
		JSONArray playerArray = data.getJSONArray("players");
		for (int i = 0; i < playerArray.length(); i++) {
			JSONObject singlePlayer = playerArray.getJSONObject(i);
			UUID singlePlayerUUID = (UUID) singlePlayer.get("uuid");
			int currentPosition = (int) singlePlayer.get("position");
			System.out.println(singlePlayer.get("name"));
			System.out.println("current position:" + currentPosition);
			JSONObject response = board.rollDice(singlePlayerUUID);
			int newPosition = (int) singlePlayer.get("position");
			System.out.println("new position:" + newPosition);
			assertThat(currentPosition).isNotEqualTo(newPosition);

		}
	}

	@Test
	   
	   public void ladder_test() throws FileNotFoundException,
	   UnsupportedEncodingException, IOException, PlayerExistsException,
	   GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException
	   { Board board = new Board(); String name1 = "shadab"; String name2 =
	   "sdgd"; String name3 = "akshay"; String name4 = "jaspal";
	   
	   board.registerPlayer(name1); board.registerPlayer(name2);
	   board.registerPlayer(name3); board.registerPlayer(name4);
	   
	   System.out.println(board.getData().getJSONArray("steps").toString());
	   JSONArray steps = new JSONArray(); steps =
	   board.getData().getJSONArray("steps"); JSONObject step = new
	   JSONObject(); for (int j = 1; j < 7; j++) { step =
	   steps.getJSONObject(j); step.put("type", 2); step.put("target", j*10);
	   } System.out.println(board.getData().getJSONArray("steps").toString());
	   JSONObject data = board.getData(); JSONArray playerArray =
	   data.getJSONArray("players"); for (int i = 0; i < 4; i++) { int turn =
	   (int) data.get("turn"); JSONObject singlePlayer =
	   playerArray.getJSONObject(turn); UUID singlePlayerUUID = (UUID)
	   singlePlayer.get("uuid");
	   
	   int currentPosition = (int) singlePlayer.get("position");
	   System.out.println(singlePlayer.get("name"));
	   System.out.println("current position:" + currentPosition); JSONObject
	   response = board.rollDice(singlePlayerUUID);
	   System.out.println(response.get("dice"));
	   System.out.println(response.get("message")); int newPosition = (int)
	   singlePlayer.get("position"); System.out.println("new position:" +
	   newPosition); assertThat(data.getJSONArray("steps").getJSONObject((int)
	   response.get("dice")).get("target")).isEqualTo(newPosition);
	   System.out.println("\n"); }
	   
	   }

	@Test

	public void snake_test() throws FileNotFoundException, UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {
		Board board = new Board();
		String name1 = "shadab";
		String name2 = "sdgd";
		String name3 = "akshay";
		String name4 = "jaspal";

		board.registerPlayer(name1);
		board.registerPlayer(name2);
		board.registerPlayer(name3);
		board.registerPlayer(name4);

		JSONObject data = board.getData();
		JSONArray playerArray = data.getJSONArray("players");
		for (int i = 0; i < playerArray.length(); i++) {
			playerArray.getJSONObject(i).put("position", 50);
		}

		System.out.println(board.getData().getJSONArray("steps").toString());
		JSONArray steps = data.getJSONArray("steps");
		JSONObject step = new JSONObject();
		for (int j = 51; j < 57; j++) {
			step = steps.getJSONObject(j);
			step.put("type", 1);
			step.put("target", 50 - (j - 50));
		}
		for (int i = 51; i < 57; i++) {
			System.out.println(data.getJSONArray("steps").getJSONObject(i).get("target"));
		}

		for (int i = 0; i < 4; i++) {
			int turn = (int) data.get("turn");
			JSONObject singlePlayer = playerArray.getJSONObject(turn);
			UUID singlePlayerUUID = (UUID) singlePlayer.get("uuid");

			int currentPosition = (int) singlePlayer.get("position");
			System.out.println(singlePlayer.get("name"));
			System.out.println("current position:" + currentPosition);
			JSONObject response = board.rollDice(singlePlayerUUID);
			System.out.println(response.get("dice"));
			System.out.println(response.get("message"));
			int newPosition = (int) singlePlayer.get("position");
			System.out.println("new position:" + newPosition);
			assertThat(data.getJSONArray("steps").getJSONObject((int) (response.get("dice")) + 50).get("target"))
					.isEqualTo(newPosition);
			System.out.println("\n");
		}

	}

	@Test(expectedExceptions = InvalidTurnException.class)

	public void checkInvalidTurnException_test()
			throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {

		Board board = new Board();
		String name1 = "shadab";

		board.registerPlayer(name1);

		board.rollDice(UUID.randomUUID());

	}

	@Test(expectedExceptions = GameInProgressException.class)

	public void GameInProgressException_test() throws FileNotFoundException, UnsupportedEncodingException, IOException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException

	{
		Board board = new Board();
		String name1 = "shadab";
		String name2 = "nishant";
		String name3 = "akshay";

		board.registerPlayer(name1);
		board.registerPlayer(name2);
		board.registerPlayer(name3);

		UUID uuid = (UUID) board.getData().getJSONArray("players").getJSONObject((int) board.getData().get("turn"))
				.get("uuid");
		board.rollDice(uuid);

		String name5 = "shad";
		board.registerPlayer(name5);

	}

	@Test

	public void IncorrectRoleOfDiceException_test()
			throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption, InvalidTurnException {
		Board board = new Board();
		String name1 = "shadab";
		String name2 = "nishant";
		String name3 = "akshay";

		board.registerPlayer(name1);
		board.registerPlayer(name2);
		board.registerPlayer(name3);

		JSONObject data = board.getData();
		JSONArray playerArray = data.getJSONArray("players");
		for (int i = 0; i < playerArray.length(); i++) {
			playerArray.getJSONObject(i).put("position", 100);
		}
		UUID uuid = (UUID) playerArray.getJSONObject((int) board.getData().get("turn")).get("uuid");
		JSONObject response = board.rollDice(uuid);
		assertThat(response.get("message")).isEqualTo("Incorrect roll of dice. Player did not move");

	}
}
