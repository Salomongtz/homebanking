tailwind.config = {
  theme: {
    extend: {
      colors: {
        clifford: '#da373d',
      },
      fontFamily: {
        'share-tech': ['Share Tech Mono', 'monospace'],
      },
      backgroundImage: {
        'gold': "url('web\assets\images\cardGold.png')",
        'silver': "url('./images/cardSilver.png')",
        'titanium': "url('./images/cardTitanium.png')",
      }
    }
  }
};
export const plugins = [];