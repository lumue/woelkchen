function save_options() {
	var serviceUrl = document.getElementById('service_url_input').value;
	chrome.storage.sync.set({
		serviceUrl: serviceUrl
	});
}

// Restores select box and checkbox state using the preferences
// stored in chrome.storage.
function restore_options() {
	chrome.storage.sync.get({serviceUrl:'http://localhost:8001/'
	}, function(items) {
		document.getElementById('service_url_input').value = items.serviceUrl
	});
}
document.addEventListener('DOMContentLoaded', restore_options);
document.getElementById('apply_button').addEventListener('click',
		save_options);