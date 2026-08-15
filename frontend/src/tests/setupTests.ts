import '@testing-library/jest-dom/vitest';

if (!window.matchMedia) {
	window.matchMedia = () => ({
		matches: false,
		media: '',
		onchange: null,
		addListener: () => { /* empty */ },
		removeListener: () => {/* empty */},
		addEventListener: () => {/* empty */},
		removeEventListener: () => {/* empty */},
		dispatchEvent: () => false,
	});
}
