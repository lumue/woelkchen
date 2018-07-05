var kodi = require('kodi-ws');



kodi('osmc-wozi', 9090).then(function (connection) {
	/* Get all active players and log them */
	return connection.Player.GetActivePlayers().then(function (players) {
		console.log('Active players:');
		console.log(JSON.stringify(players));

		/* Log the currently played item for all players */
		return Promise.all(players.map(function(player) {
			return connection.Player.GetItem(player.playerid).then(function(item) {
				console.log('Currently played for player[' + player.playerid + ']:');
				console.log(JSON.stringify(item));
			});
		}));
	});
}).catch(function(e) {
	/* Handle errors */
	if(e.stack) {
		console.error(e.stack);
	} else {
		console.error(e);
	}
})
