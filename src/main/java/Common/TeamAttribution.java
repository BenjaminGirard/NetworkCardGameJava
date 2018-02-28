package Common;

import java.util.HashMap;

public class TeamAttribution {
    public HashMap<Player, Integer> _teams;

    public TeamAttribution() {
        _teams = new HashMap<>();
    }

    public int getTeamFromId(int id) {
        int[] teamId = {0};
        _teams.forEach((player, team) -> {
            if (player.getId() == id)
                teamId[0] = team;
        });
        return teamId[0];
    }

    public void addToTeams(Player player, int team) {
        _teams.put(player, team);
    }

    public HashMap<Player, Integer> getTeams() {
        return _teams;
    }
}
