import math
import traceback

import requests
import random
import datetime
import re
f = open("queries.txt", "a", encoding="utf-8")
nouns = (
    "accelerator", "accordion", "account", "accountant", "acknowledgment", "acoustic", "acrylic", "act", "action",
    "active",
    "activity", "actor", "actress", "adapter", "addition", "address", "adjustment", "adult", "advantage",
    "advertisement",
    "advice", "afghanistan", "africa", "aftermath", "afternoon", "aftershave", "afterthought", "age", "agenda",
    "agreement",
    "air", "airbus", "airmail", "airplane", "airport", "airship", "alarm", "albatross", "alcohol", "algebra", "algeria",
    "alibi", "alley", "alligator", "alloy", "almanac", "alphabet", "alto", "aluminium", "aluminum", "ambulance",
    "america",
    "amount", "amusement", "anatomy", "anethesiologist", "anger", "angle", "angora", "animal", "anime", "ankle",
    "answer",
    "ant", "antarctica", "anteater", "antelope", "anthony", "anthropology", "apartment", "apology", "apparatus",
    "apparel",
    "appeal", "appendix", "apple", "appliance", "approval", "april", "aquarius", "arch", "archaeology", "archeology",
    "archer", "architecture", "area", "argentina", "argument", "aries", "arithmetic", "arm", "armadillo", "armchair",
    "armenian", "army", "arrow", "art", "ash", "ashtray", "asia", "asparagus", "asphalt", "asterisk", "astronomy",
    "athlete", "atm", "atom", "attack", "attempt", "attention", "attic", "attraction", "august", "aunt", "australia",
    "australian", "author", "authorisation", "authority", "authorization", "avenue", "babies", "baboon", "baby", "back",
    "backbone", "bacon", "badge", "badger", "bag", "bagel", "bagpipe", "bail", "bait", "baker", "bakery", "balance",
    "balinese", "ball", "balloon", "bamboo", "banana", "band", "bandana", "bangladesh", "bangle", "banjo", "bank",
    "bankbook", "banker", "bar", "barbara", "barber", "barge", "baritone", "barometer", "base", "baseball", "basement",
    "basin", "basket", "basketball", "bass", "bassoon", "bat", "bath", "bathroom", "bathtub", "battery", "battle",
    "bay",
    "beach", "bead", "beam", "bean", "bear", "beard", "beast", "beat", "beautician", "beauty", "beaver", "bed",
    "bedroom",
    "bee", "beech", "beef", "beer", "beet", "beetle", "beggar", "beginner", "begonia", "behavior", "belgian", "belief",
    "believe", "bell", "belt", "bench", "bengal", "beret", "berry", "bestseller", "betty", "bibliography", "bicycle",
    "bike", "bill", "billboard", "biology", "biplane", "birch", "bird", "birth", "birthday", "bit", "bite", "black",
    "bladder", "blade", "blanket", "blinker", "blizzard", "block", "blood", "blouse", "blow", "blowgun", "blue",
    "board",
    "boat", "bobcat", "body", "bolt", "bomb", "bomber", "bone", "bongo", "bonsai", "book", "bookcase", "booklet",
    "boot",
    "border", "botany", "bottle", "bottom", "boundary", "bow", "bowl", "bowling", "box", "boy", "bra", "brace",
    "bracket",
    "brain", "brake", "branch", "brand", "brandy", "brass", "brazil", "bread", "break", "breakfast", "breath", "brian",
    "brick", "bridge", "british", "broccoli", "brochure", "broker", "bronze", "brother", "brother-in-law", "brow",
    "brown",
    "brush", "bubble", "bucket", "budget", "buffer", "buffet", "bugle", "building", "bulb", "bull", "bulldozer",
    "bumper",
    "bun", "burglar", "burma", "burn", "burst", "bus", "bush", "business", "butane", "butcher", "butter", "button",
    "buzzard", "c-clamp", "cabbage", "cabinet", "cable", "cactus", "cafe", "cake", "calculator", "calculus", "calendar",
    "calf", "call", "camel", "camera", "camp", "can", "canada", "canadian", "cancer", "candle", "cannon", "canoe",
    "canvas",
    "cap", "capital", "cappelletti", "capricorn", "captain", "caption", "car", "caravan", "carbon", "card", "cardboard",
    "cardigan", "care", "carnation", "carol", "carp", "carpenter", "carriage", "carrot", "cart", "cartoon", "case",
    "cast",
    "castanet", "cat", "catamaran", "caterpillar", "cathedral", "catsup", "cattle", "cauliflower", "cause", "caution",
    "cave", "cd", "ceiling", "celery", "celeste", "cell", "cellar", "cello", "celsius", "cement", "cemetery", "cent",
    "centimeter", "century", "ceramic", "cereal", "certification", "chain", "chair", "chalk", "chance", "change",
    "channel",
    "character", "chard", "charles", "chauffeur", "check", "cheek", "cheese", "cheetah", "chef", "chemistry", "cheque",
    "cherries", "cherry", "chess", "chest", "chick", "chicken", "chicory", "chief", "child", "children", "chill",
    "chime",
    "chimpanzee", "chin", "china", "chinese", "chive", "chocolate", "chord", "christmas", "christopher", "chronometer",
    "church", "cicada", "cinema", "circle", "circulation", "cirrus", "citizenship", "city", "clam", "clarinet", "class",
    "claus", "clave", "clef", "clerk", "click", "client", "climb", "clipper", "cloakroom", "clock", "close", "closet",
    "cloth", "cloud", "cloudy", "clover", "club", "clutch", "coach", "coal", "coast", "coat", "cobweb", "cockroach",
    "cocktail", "cocoa", "cod", "coffee", "coil", "coin", "coke", "cold", "collar", "college", "collision", "colombia",
    "colon", "colony", "color", "colt", "column", "columnist", "comb", "comfort", "comic", "comma", "command",
    "commission",
    "committee", "community", "company", "comparison", "competition", "competitor", "composer", "composition",
    "computer",
    "condition", "condor", "cone", "confirmation", "conga", "congo", "conifer", "connection", "consonant", "continent",
    "control", "cook", "cooking", "copper", "copy", "copyright", "cord", "cork", "cormorant", "corn", "cornet",
    "correspondent", "cost", "cotton", "couch", "cougar", "cough", "country", "course", "court", "cousin", "cover",
    "cow",
    "cowbell", "crab", "crack", "cracker", "craftsman", "crate", "crawdad", "crayfish", "crayon", "cream", "creator",
    "creature", "credit", "creditor", "creek", "crib", "cricket", "crime", "criminal", "crocodile", "crocus",
    "croissant",
    "crook", "crop", "cross", "crow", "crowd", "crown", "crush", "cry", "cub", "cuban", "cucumber", "cultivator", "cup",
    "cupboard", "cupcake", "curler", "currency", "current", "curtain", "curve", "cushion", "custard", "customer", "cut",
    "cuticle", "cycle", "cyclone", "cylinder", "cymbal", "dad", "daffodil", "dahlia", "daisy", "damage", "dance",
    "dancer",
    "danger", "daniel", "dash", "dashboard", "database", "date", "daughter", "david", "day", "dead", "deadline", "deal",
    "death", "deborah", "debt", "debtor", "decade", "december", "decimal", "decision", "decrease", "dedication", "deer",
    "defense", "deficit", "degree", "delete", "delivery", "den", "denim", "dentist", "deodorant", "department",
    "deposit",
    "description", "desert", "design", "desire", "desk", "dessert", "destruction", "detail", "detective", "development",
    "dew", "diamond", "diaphragm", "dibble", "dictionary", "dietician", "difference", "digestion", "digger", "digital",
    "dill", "dime", "dimple", "dinghy", "dinner", "dinosaur", "diploma", "dipstick", "direction", "dirt",
    "disadvantage",
    "discovery", "discussion", "disease", "disgust", "dish", "distance", "distribution", "distributor", "diving",
    "division", "divorced", "dock", "doctor", "dog", "dogsled", "doll", "dollar", "dolphin", "domain", "donald",
    "donkey",
    "donna", "door", "dorothy", "double", "doubt", "downtown", "dragon", "dragonfly", "drain", "drake", "drama", "draw",
    "drawbridge", "drawer", "dream", "dredger", "dress", "dresser", "dressing", "drill", "drink", "drive", "driver",
    "driving", "drizzle", "drop", "drug", "drum", "dry", "dryer", "duck", "duckling", "dugout", "dungeon", "dust",
    "eagle",
    "ear", "earth", "earthquake", "ease", "east", "edge", "edger", "editor", "editorial", "education", "edward", "eel",
    "effect", "egg", "eggnog", "eggplant", "egypt", "eight", "elbow", "element", "elephant", "elizabeth", "ellipse",
    "emery", "employee", "employer", "encyclopedia", "end", "enemy", "energy", "engine", "engineer", "engineering",
    "english", "enquiry", "entrance", "environment", "epoch", "epoxy", "equinox", "equipment", "era", "error",
    "estimate",
    "ethernet", "ethiopia", "euphonium", "europe", "evening", "event", "ex-husband", "ex-wife", "examination",
    "example",
    "exchange", "exclamation", "exhaust", "existence", "expansion", "experience", "expert", "explanation", "eye",
    "eyebrow",
    "eyelash", "eyeliner", "face", "facilities", "fact", "factory", "fahrenheit", "fairies", "fall", "family", "fan",
    "fang", "farm", "farmer", "fat", "father", "father-in-law", "faucet", "fear", "feast", "feather", "feature",
    "february",
    "fedelini", "feedback", "feeling", "feet", "felony", "female", "fender", "ferry", "ferryboat", "fertilizer",
    "fiber",
    "fiberglass", "fibre", "fiction", "field", "fifth", "fight", "fighter", "file", "find", "fine", "finger", "fir",
    "fire",
    "fired", "fireman", "fireplace", "firewall", "fish", "fisherman", "flag", "flame", "flare", "flat", "flavor",
    "flax",
    "flesh", "flight", "flock", "flood", "floor", "flower", "flugelhorn", "flute", "fly", "foam", "fog", "fold", "font",
    "food", "foot", "football", "footnote", "force", "forecast", "forehead", "forest", "forgery", "fork", "form",
    "format",
    "fortnight", "foundation", "fountain", "fowl", "fox", "foxglove", "fragrance", "frame", "france", "freckle",
    "freeze",
    "freezer", "freighter", "french", "freon", "friction", "friday", "fridge", "friend", "frog", "front", "frost",
    "frown",
    "fruit", "fuel", "fur", "furniture", "galley", "gallon", "game", "gander", "garage", "garden", "garlic", "gas",
    "gasoline", "gate", "gateway", "gauge", "gazelle", "gear", "gearshift", "geese", "gemini", "gender", "geography",
    "geology", "geometry", "george", "geranium", "german", "germany", "ghana", "ghost", "giant", "giraffe", "girdle",
    "girl", "gladiolus", "glass", "glider", "gliding", "glockenspiel", "glove", "glue", "goal", "goat", "gold",
    "goldfish",
    "golf", "gondola", "gong", "good-bye", "goose", "gore-tex", "gorilla", "gosling", "government", "governor", "grade",
    "grain", "gram", "granddaughter", "grandfather", "grandmother", "grandson", "grape", "graphic", "grass",
    "grasshopper",
    "gray", "grease", "great-grandfather", "great-grandmother", "greece", "greek", "green", "grenade", "grey", "grill",
    "grip", "ground", "group", "grouse", "growth", "guarantee", "guatemalan", "guide", "guilty", "guitar", "gum", "gun",
    "gym", "gymnast", "hacksaw", "hail", "hair", "haircut", "half-brother", "half-sister", "halibut", "hall", "hallway",
    "hamburger", "hammer", "hamster", "hand", "handball", "handicap", "handle", "handsaw", "harbor", "hardboard",
    "hardcover", "hardhat", "hardware", "harmonica", "harmony", "harp", "hat", "hate", "hawk", "head", "headlight",
    "headline", "health", "hearing", "heart", "heat", "heaven", "hedge", "height", "helen", "helicopter", "helium",
    "hell",
    "helmet", "help", "hemp", "hen", "heron", "herring", "hexagon", "hill", "himalayan", "hip", "hippopotamus",
    "history",
    "hobbies", "hockey", "hoe", "hole", "holiday", "home", "honey", "hood", "hook", "hope", "horn", "horse", "hose",
    "hospital", "hot", "hour", "hourglass", "house", "hovercraft", "hub", "hubcap", "humidity", "humor", "hurricane",
    "hyacinth", "hydrant", "hydrofoil", "hydrogen", "hyena", "hygienic", "ice", "icebreaker", "icicle", "icon", "idea",
    "ikebana", "illegal", "imprisonment", "improvement", "impulse", "inch", "income", "increase", "index", "india",
    "indonesia", "industry", "ink", "innocent", "input", "insect", "instruction", "instrument", "insulation",
    "insurance",
    "interactive", "interest", "internet", "interviewer", "intestine", "invention", "inventory", "invoice", "iran",
    "iraq",
    "iris", "iron", "island", "israel", "italian", "italy", "jacket", "jaguar", "jail", "jam", "james", "january",
    "japan",
    "japanese", "jar", "jasmine", "jason", "jaw", "jeans", "jeep", "jeff", "jelly", "jellyfish", "jennifer", "jet",
    "jewel",
    "jogging", "john", "join", "joke", "joseph", "journey", "judge", "judo", "juice", "july", "jumbo", "jump", "jumper",
    "june", "jury", "justice", "jute", "kale", "kamikaze", "kangaroo", "karate", "karen", "kayak", "kendo", "kenneth",
    "kenya", "ketchup", "kettle", "kettledrum", "kevin", "key", "keyboard", "keyboarding", "kick", "kidney", "kilogram",
    "kilometer", "kimberly", "kiss", "kitchen", "kite", "kitten", "kitty", "knee", "knickers", "knife", "knight",
    "knot",
    "knowledge", "kohlrabi", "korean", "laborer", "lace", "ladybug", "lake", "lamb", "lamp", "lan", "land", "landmine",
    "language", "larch", "lasagna", "latency", "latex", "lathe", "laugh", "laundry", "laura", "law", "lawyer", "layer",
    "lead", "leaf", "learning", "leather", "leek", "leg", "legal", "lemonade", "lentil", "leo", "leopard", "letter",
    "lettuce", "level", "libra", "library", "license", "lier", "lift", "light", "lightning", "lilac", "lily", "limit",
    "linda", "line", "linen", "link", "lion", "lip", "lipstick", "liquid", "liquor", "lisa", "list", "literature",
    "litter",
    "liver", "lizard", "llama", "loaf", "loan", "lobster", "lock", "locket", "locust", "look", "loss", "lotion", "love",
    "low", "lumber", "lunch", "lunchroom", "lung", "lunge", "lute", "luttuce", "lycra", "lynx", "lyocell", "lyre",
    "lyric",
    "macaroni", "machine", "macrame", "magazine", "magic", "magician", "maid", "mail", "mailbox", "mailman", "makeup",
    "malaysia", "male", "mall", "mallet", "man", "manager", "mandolin", "manicure", "manx", "map", "maple", "maraca",
    "marble", "march", "margaret", "margin", "maria", "marimba", "mark", "market", "married", "mary", "mascara", "mask",
    "mass", "match", "math", "mattock", "may", "mayonnaise", "meal", "measure", "meat", "mechanic", "medicine",
    "meeting",
    "melody", "memory", "men", "menu", "mercury", "message", "metal", "meteorology", "meter", "methane", "mexican",
    "mexico", "mice", "michael", "michelle", "microwave", "middle", "mile", "milk", "milkshake", "millennium",
    "millimeter",
    "millisecond", "mimosa", "mind", "mine", "mini-skirt", "minibus", "minister", "mint", "minute", "mirror", "missile",
    "mist", "mistake", "mitten", "moat", "modem", "mole", "mom", "monday", "money", "monkey", "month", "moon",
    "morning",
    "morocco", "mosque", "mosquito", "mother", "mother-in-law", "motion", "motorboat", "motorcycle", "mountain",
    "mouse",
    "moustache", "mouth", "move", "multi-hop", "multimedia", "muscle", "museum", "music", "musician", "mustard",
    "myanmar",
    "nail", "name", "nancy", "napkin", "narcissus", "nation", "neck", "need", "needle", "neon", "nepal", "nephew",
    "nerve",
    "nest", "net", "network", "news", "newsprint", "newsstand", "nic", "nickel", "niece", "nigeria", "night",
    "nitrogen",
    "node", "noise", "noodle", "north", "north america", "north korea", "norwegian", "nose", "note", "notebook",
    "notify",
    "novel", "november", "number", "numeric", "nurse", "nut", "nylon", "oak", "oatmeal", "objective", "oboe",
    "observation",
    "occupation", "ocean", "ocelot", "octagon", "octave", "october", "octopus", "odometer", "offence", "offer",
    "office",
    "oil", "okra", "olive", "onion", "open", "opera", "operation", "ophthalmologist", "opinion", "option", "orange",
    "orchestra", "orchid", "order", "organ", "organisation", "organization", "ornament", "ostrich", "otter", "ounce",
    "output", "outrigger", "oval", "oven", "overcoat", "owl", "owner", "ox", "oxygen", "oyster", "package", "packet",
    "page", "pail", "pain", "paint", "pair", "pajama", "pakistan", "palm", "pamphlet", "pan", "pancake", "pancreas",
    "panda", "pansy", "panther", "panties", "pantry", "pants", "panty", "pantyhose", "paper", "paperback", "parade",
    "parallelogram", "parcel", "parent", "parentheses", "park", "parrot", "parsnip", "part", "particle", "partner",
    "partridge", "party", "passbook", "passenger", "passive", "pasta", "paste", "pastor", "pastry", "patch", "path",
    "patient", "patio", "patricia", "paul", "payment", "pea", "peace", "peak", "peanut", "pear", "pedestrian",
    "pediatrician", "peen", "peer-to-peer", "pelican", "pen", "penalty", "pencil", "pendulum", "pentagon", "peony",
    "pepper", "perch", "perfume", "period", "periodical", "peripheral", "permission", "persian", "person", "peru",
    "pest",
    "pet", "pharmacist", "pheasant", "philippines", "philosophy", "phone", "physician", "piano", "piccolo", "pickle",
    "picture", "pie", "pig", "pigeon", "pike", "pillow", "pilot", "pimple", "pin", "pine", "ping", "pink", "pint",
    "pipe",
    "pisces", "pizza", "place", "plain", "plane", "planet", "plant", "plantation", "plaster", "plasterboard", "plastic",
    "plate", "platinum", "play", "playground", "playroom", "pleasure", "plier", "plot", "plough", "plow", "plywood",
    "pocket", "poet", "point", "poison", "poland", "police", "policeman", "polish", "politician", "pollution", "polo",
    "polyester", "pond", "popcorn", "poppy", "population", "porch", "porcupine", "port", "porter", "position",
    "possibility", "postage", "postbox", "pot", "potato", "poultry", "pound", "powder", "power", "precipitation",
    "preface",
    "prepared", "pressure", "price", "priest", "print", "printer", "prison", "probation", "process", "processing",
    "produce", "product", "production", "professor", "profit", "promotion", "propane", "property", "prose",
    "prosecution",
    "protest", "protocol", "pruner", "psychiatrist", "psychology", "ptarmigan", "puffin", "pull", "puma", "pump",
    "pumpkin",
    "punch", "punishment", "puppy", "purchase", "purple", "purpose", "push", "pvc", "pyjama", "pyramid", "quail",
    "quality",
    "quart", "quarter", "quartz", "queen", "question", "quicksand", "quiet", "quill", "quilt", "quince", "quit",
    "quiver",
    "quotation", "rabbi", "rabbit", "racing", "radar", "radiator", "radio", "radish", "raft", "rail", "railway", "rain",
    "rainbow", "raincoat", "rainstorm", "rake", "ramie", "random", "range", "rat", "rate", "raven", "ravioli", "ray",
    "rayon", "reaction", "reading", "reason", "receipt", "recess", "record", "recorder", "rectangle", "red",
    "reduction",
    "refrigerator", "refund", "regret", "reindeer", "relation", "relative", "religion", "relish", "reminder", "repair",
    "replace", "report", "representative", "request", "resolution", "respect", "responsibility", "rest", "restaurant",
    "result", "retailer", "revolve", "revolver", "reward", "rhinoceros", "rhythm", "rice", "richard", "riddle", "rifle",
    "ring", "rise", "risk", "river", "riverbed", "road", "roadway", "roast", "robert", "robin", "rock", "rocket", "rod",
    "roll", "romania", "romanian", "ronald", "roof", "room", "rooster", "root", "rose", "rotate", "route", "router",
    "rowboat", "rub", "rubber", "rugby", "rule", "run", "russia", "russian", "rutabaga", "ruth", "sack", "sagittarius",
    "sail", "sailboat", "sailor", "salad", "salary", "sale", "salesman", "salmon", "salt", "sampan", "samurai", "sand",
    "sandra", "sandwich", "santa", "sarah", "sardine", "satin", "saturday", "sauce", "saudi arabia", "sausage", "save",
    "saw", "saxophone", "scale", "scallion", "scanner", "scarecrow", "scarf", "scene", "scent", "schedule", "school",
    "science", "scissors", "scooter", "scorpio", "scorpion", "scraper", "screen", "screw", "screwdriver", "sea",
    "seagull",
    "seal", "seaplane", "search", "seashore", "season", "seat", "second", "secretary", "secure", "security", "seed",
    "seeder", "segment", "select", "selection", "self", "semicircle", "semicolon", "sense", "sentence", "separated",
    "september", "servant", "server", "session", "sex", "shade", "shadow", "shake", "shallot", "shame", "shampoo",
    "shape",
    "share", "shark", "sharon", "shears", "sheep", "sheet", "shelf", "shell", "shield", "shingle", "ship", "shirt",
    "shock",
    "shoe", "shoemaker", "shop", "shorts", "shoulder", "shovel", "show", "shrimp", "shrine", "siamese", "siberian",
    "side",
    "sideboard", "sidecar", "sidewalk", "sign", "signature", "silica", "silk", "silver", "sing", "singer", "single",
    "sink",
    "sister", "sister-in-law", "size", "skate", "skiing", "skill", "skin", "skirt", "sky", "slash", "slave", "sled",
    "sleep", "sleet", "slice", "slime", "slip", "slipper", "slope", "smash", "smell", "smile", "smoke", "snail",
    "snake",
    "sneeze", "snow", "snowboarding", "snowflake", "snowman", "snowplow", "snowstorm", "soap", "soccer", "society",
    "sociology", "sock", "soda", "sofa", "softball", "softdrink", "software", "soil", "soldier", "son", "song",
    "soprano",
    "sort", "sound", "soup", "sousaphone", "south africa", "south america", "south korea", "soy", "soybean", "space",
    "spade", "spaghetti", "spain", "spandex", "spark", "sparrow", "spear", "specialist", "speedboat", "sphere",
    "sphynx",
    "spider", "spike", "spinach", "spleen", "sponge", "spoon", "spot", "spring", "sprout", "spruce", "spy", "square",
    "squash", "squid", "squirrel", "stage", "staircase", "stamp", "star", "start", "starter", "state", "statement",
    "station", "statistic", "steam", "steel", "stem", "step", "step-aunt", "step-brother", "step-daughter",
    "step-father",
    "step-grandfather", "step-grandmother", "step-mother", "step-sister", "step-son", "step-uncle", "stepdaughter",
    "stepmother", "stepson", "steven", "stew", "stick", "stinger", "stitch", "stock", "stocking", "stomach", "stone",
    "stool", "stop", "stopsign", "stopwatch", "store", "storm", "story", "stove", "stranger", "straw", "stream",
    "street",
    "streetcar", "stretch", "string", "structure", "study", "sturgeon", "submarine", "substance", "subway", "success",
    "sudan", "suede", "sugar", "suggestion", "suit", "summer", "sun", "sunday", "sundial", "sunflower", "sunshine",
    "supermarket", "supply", "support", "surfboard", "surgeon", "surname", "surprise", "susan", "sushi", "swallow",
    "swamp",
    "swan", "sweater", "sweatshirt", "sweatshop", "swedish", "sweets", "swim", "swimming", "swing", "swiss", "switch",
    "sword", "swordfish", "sycamore", "syria", "syrup", "system", "t-shirt", "table", "tablecloth", "tabletop",
    "tachometer", "tadpole", "tail", "tailor", "taiwan", "talk", "tank", "tanker", "tanzania", "target", "taste",
    "taurus",
    "tax", "taxi", "taxicab", "tea", "teacher", "teaching", "team", "technician", "teeth", "television", "teller",
    "temper",
    "temperature", "temple", "tempo", "tendency", "tennis", "tenor", "tent", "territory", "test", "text", "textbook",
    "texture", "thailand", "theater", "theory", "thermometer", "thing", "thistle", "thomas", "thought", "thread",
    "thrill",
    "throat", "throne", "thumb", "thunder", "thunderstorm", "thursday", "ticket", "tie", "tiger", "tights", "tile",
    "timbale", "time", "timer", "timpani", "tin", "tip", "tire", "titanium", "title", "toad", "toast", "toe", "toenail",
    "toilet", "tom-tom", "tomato", "ton", "tongue", "tooth", "toothbrush", "toothpaste", "top", "tornado", "tortellini",
    "tortoise", "touch", "tower", "town", "toy", "tractor", "trade", "traffic", "trail", "train", "tramp",
    "transaction",
    "transmission", "transport", "trapezoid", "tray", "treatment", "tree", "trial", "triangle", "trick", "trigonometry",
    "trip", "trombone", "trouble", "trousers", "trout", "trowel", "truck", "trumpet", "trunk", "tsunami", "tub", "tuba",
    "tuesday", "tugboat", "tulip", "tuna", "tune", "turkey", "turkish", "turn", "turnip", "turnover", "turret",
    "turtle",
    "tv", "twig", "twilight", "twine", "twist", "typhoon", "tyvek", "uganda", "ukraine", "ukrainian", "umbrella",
    "uncle",
    "underclothes", "underpants", "undershirt", "underwear", "unit", "united kingdom", "unshielded", "use", "utensil",
    "uzbekistan", "vacation", "vacuum", "valley", "value", "van", "var verbs = [aardvark", "vase", "vault", "vegetable",
    "vegetarian", "veil", "vein", "velvet", "venezuela", "venezuelan", "verdict", "vermicelli", "verse", "vessel",
    "vest",
    "veterinarian", "vibraphone", "vietnam", "view", "vinyl", "viola", "violet", "violin", "virgo", "viscose", "vise",
    "vision", "visitor", "voice", "volcano", "volleyball", "voyage", "vulture", "waiter", "waitress", "walk", "wall",
    "wallaby", "wallet", "walrus", "war", "warm", "wash", "washer", "wasp", "waste", "watch", "watchmaker", "water",
    "waterfall", "wave", "wax", "way", "wealth", "weapon", "weasel", "weather", "wedge", "wednesday", "weed", "weeder",
    "week", "weight", "whale", "wheel", "whip", "whiskey", "whistle", "white", "wholesaler", "whorl", "wilderness",
    "william", "willow", "wind", "windchime", "window", "windscreen", "windshield", "wine", "wing", "winter", "wire",
    "wish", "witch", "withdrawal", "witness", "wolf", "woman", "women", "wood", "wool", "woolen", "word", "work",
    "workshop", "worm", "wound", "wrecker", "wren", "wrench", "wrinkle", "wrist", "writer", "xylophone", "yacht", "yak",
    "yam", "yard", "yarn", "year", "yellow", "yew", "yogurt", "yoke", "yugoslavian", "zebra", "zephyr", "zinc",
    "zipper",
    "zone", "zoo", "zoology")
verbs = (
    "accept", "add", "admire", "admit", "advise", "afford", "agree", "alert", "allow", "amuse", "analyse", "announce",
    "annoy", "answer", "apologise", "appear", "applaud", "appreciate", "approve", "argue", "arrange", "arrest",
    "arrive",
    "ask", "attach", "attack", "attempt", "attend", "attract", "avoid", "back", "bake", "balance", "ban", "bang",
    "bare",
    "bat", "bathe", "battle", "beam", "beg", "behave", "belong", "bleach", "bless", "blind", "blink", "blot", "blush",
    "boast", "boil", "bolt", "bomb", "book", "bore", "borrow", "bounce", "bow", "box", "brake", "branch", "breathe",
    "bruise", "brush", "bubble", "bump", "burn", "bury", "buzz", "calculate", "call", "camp", "care", "carry", "carve",
    "cause", "challenge", "change", "charge", "chase", "cheat", "check", "cheer", "chew", "choke", "chop", "claim",
    "clap",
    "clean", "clear", "clip", "close", "coach", "coil", "collect", "colour", "comb", "command", "communicate",
    "compare",
    "compete", "complain", "complete", "concentrate", "concern", "confess", "confuse", "connect", "consider", "consist",
    "contain", "continue", "copy", "correct", "cough", "count", "cover", "crack", "crash", "crawl", "cross", "crush",
    "cry",
    "cure", "curl", "curve", "cycle", "dam", "damage", "dance", "dare", "decay", "deceive", "decide", "decorate",
    "delay",
    "delight", "deliver", "depend", "describe", "desert", "deserve", "destroy", "detect", "develop", "disagree",
    "disappear", "disapprove", "disarm", "discover", "dislike", "divide", "double", "doubt", "drag", "drain", "dream",
    "dress", "drip", "drop", "drown", "drum", "dry", "dust", "earn", "educate", "embarrass", "employ", "empty",
    "encourage",
    "end", "enjoy", "enter", "entertain", "escape", "examine", "excite", "excuse", "exercise", "exist", "expand",
    "expect",
    "explain", "explode", "extend", "face", "fade", "fail", "fancy", "fasten", "fax", "fear", "fence", "fetch", "file",
    "fill", "film", "fire", "fit", "fix", "flap", "flash", "float", "flood", "flow", "flower", "fold", "follow", "fool",
    "force", "form", "found", "frame", "frighten", "fry", "gather", "gaze", "glow", "glue", "grab", "grate", "grease",
    "greet", "grin", "grip", "groan", "guarantee", "guard", "guess", "guide", "hammer", "hand", "handle", "hang",
    "happen",
    "harass", "harm", "hate", "haunt", "head", "heal", "heap", "heat", "help", "hook", "hop", "hope", "hover", "hug",
    "hum",
    "hunt", "hurry", "identify", "ignore", "imagine", "impress", "improve", "include", "increase", "influence",
    "inform",
    "inject", "injure", "instruct", "intend", "interest", "interfere", "interrupt", "introduce", "invent", "invite",
    "irritate", "itch", "jail", "jam", "jog", "join", "joke", "judge", "juggle", "jump", "kick", "kill", "kiss",
    "kneel",
    "knit", "knock", "knot", "label", "land", "last", "laugh", "launch", "learn", "level", "license", "lick", "lie",
    "lighten", "like", "list", "listen", "live", "load", "lock", "long", "look", "love", "man", "manage", "march",
    "mark",
    "marry", "match", "mate", "matter", "measure", "meddle", "melt", "memorise", "mend", "mess up", "milk", "mine",
    "miss",
    "mix", "moan", "moor", "mourn", "move", "muddle", "mug", "multiply", "murder", "nail", "name", "need", "nest",
    "nod",
    "note", "notice", "number", "obey", "object", "observe", "obtain", "occur", "offend", "offer", "open", "order",
    "overflow", "owe", "own", "pack", "paddle", "paint", "park", "part", "pass", "paste", "pat", "pause", "peck",
    "pedal",
    "peel", "peep", "perform", "permit", "phone", "pick", "pinch", "pine", "place", "plan", "plant", "play", "please",
    "plug", "point", "poke", "polish", "pop", "possess", "post", "pour", "practise", "pray", "preach", "precede",
    "prefer",
    "prepare", "present", "preserve", "press", "pretend", "prevent", "prick", "print", "produce", "program", "promise",
    "protect", "provide", "pull", "pump", "punch", "puncture", "punish", "push", "question", "queue", "race", "radiate",
    "rain", "raise", "reach", "realise", "receive", "recognise", "record", "reduce", "reflect", "refuse", "regret",
    "reign",
    "reject", "rejoice", "relax", "release", "rely", "remain", "remember", "remind", "remove", "repair", "repeat",
    "replace", "reply", "report", "reproduce", "request", "rescue", "retire", "return", "rhyme", "rinse", "risk", "rob",
    "rock", "roll", "rot", "rub", "ruin", "rule", "rush", "sack", "sail", "satisfy", "save", "saw", "scare", "scatter",
    "scold", "scorch", "scrape", "scratch", "scream", "screw", "scribble", "scrub", "seal", "search", "separate",
    "serve",
    "settle", "shade", "share", "shave", "shelter", "shiver", "shock", "shop", "shrug", "sigh", "sign", "signal", "sin",
    "sip", "ski", "skip", "slap", "slip", "slow", "smash", "smell", "smile", "smoke", "snatch", "sneeze", "sniff",
    "snore",
    "snow", "soak", "soothe", "sound", "spare", "spark", "sparkle", "spell", "spill", "spoil", "spot", "spray",
    "sprout",
    "squash", "squeak", "squeal", "squeeze", "stain", "stamp", "stare", "start", "stay", "steer", "step", "stir",
    "stitch",
    "stop", "store", "strap", "strengthen", "stretch", "strip", "stroke", "stuff", "subtract", "succeed", "suck",
    "suffer",
    "suggest", "suit", "supply", "support", "suppose", "surprise", "surround", "suspect", "suspend", "switch", "talk",
    "tame", "tap", "taste", "tease", "telephone", "tempt", "terrify", "test", "thank", "thaw", "tick", "tickle", "tie",
    "time", "tip", "tire", "touch", "tour", "tow", "trace", "trade", "train", "transport", "trap", "travel", "treat",
    "tremble", "trick", "trip", "trot", "trouble", "trust", "try", "tug", "tumble", "turn", "twist", "type", "undress",
    "unfasten", "unite", "unlock", "unpack", "untidy", "use", "vanish", "visit", "wail", "wait", "walk", "wander",
    "want",
    "warm", "warn", "wash", "waste", "watch", "water", "wave", "weigh", "welcome", "whine", "whip", "whirl", "whisper",
    "whistle", "wink", "wipe", "wish", "wobble", "wonder", "work", "worry", "wrap", "wreck", "wrestle", "wriggle",
    "x-ray",
    "yawn", "yell", "zip", "zoom")
adj = (
    "aback", "abaft", "abandoned", "abashed", "aberrant", "abhorrent", "abiding", "abject", "ablaze", "able",
    "abnormal",
    "aboard", "aboriginal", "abortive", "abounding", "abrasive", "abrupt", "absent", "absorbed", "absorbing",
    "abstracted",
    "absurd", "abundant", "abusive", "acceptable", "accessible", "accidental", "accurate", "acid", "acidic", "acoustic",
    "acrid", "actually", "ad", "hoc", "adamant", "adaptable", "addicted", "adhesive", "adjoining", "adorable",
    "adventurous", "afraid", "aggressive", "agonizing", "agreeable", "ahead", "ajar", "alcoholic", "alert", "alike",
    "alive", "alleged", "alluring", "aloof", "amazing", "ambiguous", "ambitious", "amuck", "amused", "amusing",
    "ancient",
    "angry", "animated", "annoyed", "annoying", "anxious", "apathetic", "aquatic", "aromatic", "arrogant", "ashamed",
    "aspiring", "assorted", "astonishing", "attractive", "auspicious", "automatic", "available", "average", "awake",
    "aware", "awesome", "awful", "axiomatic", "bad", "barbarous", "bashful", "bawdy", "beautiful", "befitting",
    "belligerent", "beneficial", "bent", "berserk", "best", "better", "bewildered", "big", "billowy", "bite-sized",
    "bitter", "bizarre", "black", "black-and-white", "bloody", "blue", "blue-eyed", "blushing", "boiling", "boorish",
    "bored", "boring", "bouncy", "boundless", "brainy", "brash", "brave", "brawny", "breakable", "breezy", "brief",
    "bright", "bright", "broad", "broken", "brown", "bumpy", "burly", "bustling", "busy", "cagey", "calculating",
    "callous",
    "calm", "capable", "capricious", "careful", "careless", "caring", "cautious", "ceaseless", "certain", "changeable",
    "charming", "cheap", "cheerful", "chemical", "chief", "childlike", "chilly", "chivalrous", "chubby", "chunky",
    "clammy",
    "classy", "clean", "clear", "clever", "cloistered", "cloudy", "closed", "clumsy", "cluttered", "coherent", "cold",
    "colorful", "colossal", "combative", "comfortable", "common", "complete", "complex", "concerned", "condemned",
    "confused", "conscious", "cooing", "cool", "cooperative", "coordinated", "courageous", "cowardly", "crabby",
    "craven",
    "crazy", "creepy", "crooked", "crowded", "cruel", "cuddly", "cultured", "cumbersome", "curious", "curly", "curved",
    "curvy", "cut", "cute", "cute", "cynical", "daffy", "daily", "damaged", "damaging", "damp", "dangerous", "dapper",
    "dark", "dashing", "dazzling", "dead", "deadpan", "deafening", "dear", "debonair", "decisive", "decorous", "deep",
    "deeply", "defeated", "defective", "defiant", "delicate", "delicious", "delightful", "demonic", "delirious",
    "dependent", "depressed", "deranged", "descriptive", "deserted", "detailed", "determined", "devilish", "didactic",
    "different", "difficult", "diligent", "direful", "dirty", "disagreeable", "disastrous", "discreet", "disgusted",
    "disgusting", "disillusioned", "dispensable", "distinct", "disturbed", "divergent", "dizzy", "domineering",
    "doubtful",
    "drab", "draconian", "dramatic", "dreary", "drunk", "dry", "dull", "dusty", "dynamic", "dysfunctional", "eager",
    "early", "earsplitting", "earthy", "easy", "eatable", "economic", "educated", "efficacious", "efficient", "eight",
    "elastic", "elated", "elderly", "electric", "elegant", "elfin", "elite", "embarrassed", "eminent", "empty",
    "enchanted",
    "enchanting", "encouraging", "endurable", "energetic", "enormous", "entertaining", "enthusiastic", "envious",
    "equable",
    "equal", "erect", "erratic", "ethereal", "evanescent", "evasive", "even excellent excited", "exciting exclusive",
    "exotic", "expensive", "extra-large extra-small exuberant", "exultant", "fabulous", "faded", "faint fair",
    "faithful",
    "fallacious", "false familiar famous", "fanatical", "fancy", "fantastic", "far", " far-flung", " fascinated",
    "fast",
    "fat faulty", "fearful fearless", "feeble feigned", "female fertile", "festive", "few fierce", "filthy", "fine",
    "finicky", "first", " five", " fixed", " flagrant", "flaky", "flashy", "flat", "flawless", "flimsy", " flippant",
    "flowery", "fluffy", "fluttering", " foamy", "foolish", "foregoing", "forgetful", "fortunate", "four frail",
    "fragile",
    "frantic", "free", " freezing", " frequent", " fresh", " fretful", "friendly",
    "frightened frightening full fumbling functional", "funny", "furry furtive", "future futuristic", "fuzzy ", "gabby",
    "gainful", "gamy", "gaping", "garrulous", "gaudy", "general gentle", "giant", "giddy", "gifted", "gigantic",
    "glamorous", "gleaming", "glib", "glistening glorious", "glossy", "godly", "good", "goofy", "gorgeous", "graceful",
    "grandiose", "grateful gratis", "gray greasy great", "greedy", "green grey grieving", "groovy", "grotesque",
    "grouchy",
    "grubby gruesome", "grumpy", "guarded", "guiltless", "gullible gusty", "guttural H habitual", "half", "hallowed",
    "halting", "handsome", "handsomely", "handy", "hanging", "hapless", "happy", "hard", "hard-to-find", "harmonious",
    "harsh", "hateful", "heady", "healthy", "heartbreaking", "heavenly heavy hellish", "helpful", "helpless",
    "hesitant",
    "hideous high", "highfalutin", "high-pitched", "hilarious", "hissing", "historical", "holistic", "hollow",
    "homeless",
    "homely", "honorable", "horrible", "hospitable", "hot huge", "hulking", "humdrum", "humorous", "hungry", "hurried",
    "hurt", "hushed", "husky", "hypnotic", "hysterical", "icky", "icy", "idiotic", "ignorant", "ill", "illegal",
    "ill-fated", "ill-informed", "illustrious", "imaginary", "immense", "imminent", "impartial", "imperfect",
    "impolite",
    "important", "imported", "impossible", "incandescent", "incompetent", "inconclusive", "industrious", "incredible",
    "inexpensive", "infamous", "innate", "innocent", "inquisitive", "insidious", "instinctive", "intelligent",
    "interesting", "internal", "invincible", "irate", "irritating", "itchy", "jaded", "jagged", "jazzy", "jealous",
    "jittery", "jobless", "jolly", "joyous", "judicious", "juicy", "jumbled", "jumpy", "juvenile", "kaput", "keen",
    "kind",
    "kindhearted", "kindly", "knotty", "knowing", "knowledgeable", "known", "labored", "lackadaisical", "lacking",
    "lame",
    "lamentable", "languid", "large", "last", "late", "laughable", "lavish", "lazy", "lean", "learned", "left", "legal",
    "lethal", "level", "lewd", "light", "like", "likeable", "limping", "literate", "little", "lively", "lively",
    "living",
    "lonely", "long", "longing", "long-term", "loose", "lopsided", "loud", "loutish", "lovely", "loving", "low",
    "lowly",
    "lucky", "ludicrous", "lumpy", "lush", "luxuriant", "lying", "lyrical", "macabre", "macho", "maddening", "madly",
    "magenta", "magical", "magnificent", "majestic", "makeshift", "male", "malicious", "mammoth", "maniacal", "many",
    "marked", "massive", "married", "marvelous", "material", "materialistic", "mature", "mean", "measly", "meaty",
    "medical", "meek", "mellow", "melodic", "melted", "merciful", "mere", "messy", "mighty", "military", "milky",
    "mindless", "miniature", "minor", "miscreant", "misty", "mixed", "moaning", "modern", "moldy", "momentous",
    "motionless", "mountainous", "muddled", "mundane", "murky", "mushy", "mute", "mysterious", "naive", "nappy",
    "narrow",
    "nasty", "natural", "naughty", "nauseating", "near", "neat", "nebulous", "necessary", "needless", "needy",
    "neighborly",
    "nervous", "new", "next", "nice", "nifty", "nimble", "nine", "nippy", "noiseless", "noisy", "nonchalant",
    "nondescript",
    "nonstop", "normal", "nostalgic", "nosy", "noxious", "null", "numberless", "numerous", "nutritious", "nutty",
    "oafish",
    "obedient", "obeisant", "obese", "obnoxious", "obscene", "obsequious", "observant", "obsolete", "obtainable",
    "oceanic",
    "odd", "offbeat", "old", "old-fashioned", "omniscient", "one", "onerous", "open", "opposite", "optimal", "orange",
    "ordinary", "organic", "ossified", "outgoing", "outrageous", "outstanding", "oval", "overconfident", "overjoyed",
    "overrated", "overt", "overwrought", "painful", "painstaking", "pale", "paltry", "panicky", "panoramic", "parallel",
    "parched", "parsimonious", "past", "pastoral", "pathetic", "peaceful", "penitent", "perfect", "periodic",
    "permissible",
    "perpetual", "petite", "petite", "phobic", "physical", "picayune", "pink", "piquant", "placid", "plain", "plant",
    "plastic", "plausible", "pleasant", "plucky", "pointless", "poised", "polite", "political", "poor", "possessive",
    "possible", "powerful", "precious", "premium", "present", "pretty", "previous", "pricey", "prickly", "private",
    "probable", "productive", "profuse", "protective", "proud", "psychedelic", "psychotic", "public", "puffy", "pumped",
    "puny", "purple", "purring", "pushy", "puzzled", "puzzling", "quack", "quaint", "quarrelsome", "questionable",
    "quick",
    "quickest", "quiet", "quirky", "quixotic", "quizzical", "rabid", "racial", "ragged", "rainy", "rambunctious",
    "rampant",
    "rapid", "rare", "raspy", "ratty", "ready", "real", "rebel", "receptive", "recondite", "red", "redundant",
    "reflective",
    "regular", "relieved", "remarkable", "reminiscent", "repulsive", "resolute", "resonant", "responsible",
    "rhetorical",
    "rich", "right", "righteous", "rightful", "rigid", "ripe", "ritzy", "roasted", "robust", "romantic", "roomy",
    "rotten",
    "rough", "round", "royal", "ruddy", "rude", "rural", "rustic", "ruthless", "sable", "sad", "safe", "salty", "same",
    "sassy", "satisfying", "savory", "scandalous", "scarce", "scared", "scary", "scattered", "scientific",
    "scintillating",
    "scrawny", "screeching", "second", "second-hand", "secret", "secretive", "sedate", "seemly", "selective", "selfish",
    "separate", "serious", "shaggy", "shaky", "shallow", "sharp", "shiny", "shivering", "shocking", "short", "shrill",
    "shut", "shy", "sick", "silent", "silent", "silky", "silly", "simple", "simplistic", "sincere", "six", "skillful",
    "skinny", "sleepy", "slim", "slimy", "slippery", "sloppy", "slow", "small", "smart", "smelly", "smiling", "smoggy",
    "smooth", "sneaky", "snobbish", "snotty", "soft", "soggy", "solid", "somber", "sophisticated", "sordid", "sore",
    "sore",
    "sour", "sparkling", "special", "spectacular", "spicy", "spiffy", "spiky", "spiritual", "spiteful", "splendid",
    "spooky", "spotless", "spotted", "spotty", "spurious", "squalid", "square", "squealing", "squeamish", "staking",
    "stale", "standing", "statuesque", "steadfast", "steady", "steep", "stereotyped", "sticky", "stiff", "stimulating",
    "stingy", "stormy", "straight", "strange", "striped", "strong", "stupendous", "stupid", "sturdy", "subdued",
    "subsequent", "substantial", "successful", "succinct", "sudden", "sulky", "super", "superb", "superficial",
    "supreme",
    "swanky", "sweet", "sweltering", "swift", "symptomatic", "synonymous", "taboo", "tacit", "tacky", "talented",
    "tall",
    "tame", "tan", "tangible", "tangy", "tart", "tasteful", "tasteless", "tasty", "tawdry", "tearful", "tedious",
    "teeny",
    "teeny-tiny", "telling", "temporary", "ten", "tender tense", "tense", "tenuous", "terrible", "terrific", "tested",
    "testy", "thankful", "therapeutic", "thick", "thin", "thinkable", "third", "thirsty", "thoughtful", "thoughtless",
    "threatening", "three", "thundering", "tidy", "tight", "tightfisted", "tiny", "tired", "tiresome", "toothsome",
    "torpid", "tough", "towering", "tranquil", "trashy", "tremendous", "tricky", "trite", "troubled", "truculent",
    "true",
    "truthful", "two", "typical", "ubiquitous", "ugliest", "ugly", "ultra", "unable", "unaccountable", "unadvised",
    "unarmed", "unbecoming", "unbiased", "uncovered", "understood", "undesirable", "unequal", "unequaled", "uneven",
    "unhealthy", "uninterested", "unique", "unkempt", "unknown", "unnatural", "unruly", "unsightly", "unsuitable",
    "untidy",
    "unused", "unusual", "unwieldy", "unwritten", "upbeat", "uppity", "upset", "uptight", "used", "useful", "useless",
    "utopian", "utter", "uttermost", "vacuous", "vagabond", "vague", "valuable", "various", "vast", "vengeful",
    "venomous",
    "verdant", "versed", "victorious", "vigorous", "violent", "violet", "vivacious", "voiceless", "volatile",
    "voracious",
    "vulgar", "wacky", "waggish", "waiting", "", "wakeful", "wandering", "wanting", "warlike", "warm", "wary",
    "wasteful",
    "watery", "weak", "wealthy", "weary", "well-groomed", "well-made", "well-off", "well-to-do", "wet", "whimsical",
    "whispering", "white", "whole", "wholesale", "wicked", "wide", "wide-eyed", "wiggly", "wild", "willing", "windy",
    "wiry", "wise", "wistful", "witty", "woebegone", "womanly", "wonderful", "wooden", "woozy", "workable", "worried",
    "worthless", "wrathful", "wretched", "wrong", "wry", "xenophobic", "yellow", "yielding", "young", "youthful",
    "yummy",
    "zany", "zealous", "zesty", "zippy", "zonked");

idUser = 1
idAttivita = 1
idProdotto = 1
idDispensa = 1
idAvviso = 1
idCategoria = 1
idPiatto = 1
idMenu = 1

ruolo = ["admin", "supervisore", "addetto_cucina", "addetto_sala"]

NUM_MAX_OF_AVVISI_NASCOSTI = 4
NUMBER_OF_USER = 35
NUMBER_OF_ATTIVITA = 10
NUMBER_OF_PIATTI = 100
NUMBER_OF_AVVISI = 100
NUMBER_OF_MENU = NUMBER_OF_ATTIVITA
users = []
attivita = []
avvisi = []
categorieMenu = []


def existAdminInAttivita(id_attivita):
    global users
    for u in users:
        if u.getIdAttivita() == id_attivita and u.getRole() == "admin":
            return u.getIdUser()
    return -1


def generateUser():
    global idUser, ruolo, users, NUMBER_OF_USER
    query = ""
    numofadmin = 0
    i = 0
    while i < NUMBER_OF_USER:
        repeat = False
        while not repeat:
            repeat = False
            response = requests.get("https://randomuser.me/api/")
            js = response.json()
            isUtf = js['results'][0]['name']['first']
            if re.search('[a-zA-Z]', isUtf):
                repeat = True

        apici = "'"
        name = apici + js['results'][0]['name']['first'] + apici
        surname = apici + js['results'][0]['name']['last'] + apici
        password = apici + js['results'][0]['login']['password'] + apici
        datanascita = apici + js['results'][0]['dob']['date'] + apici
        dataaggiunta = apici + js['results'][0]['registered']['date'] + apici
        if random.randint(0, 1) == 0:
            isFirstAccess = "false"
        else:
            isFirstAccess = "true"

        email = apici + js['results'][0]['email'] + apici
        role = ruolo[random.randint(0, 3)]
        id_attivita = random.randint(1, NUMBER_OF_ATTIVITA)
        tmp = existAdminInAttivita(id_attivita)
        if tmp != -1:
            role = ruolo[random.randint(1, 3)]

        if role == "admin":
            aggiuntoda = -1
        else:
            tmp = existAdminInAttivita(id_attivita)
            if tmp != -1:
                aggiuntoda = tmp
            else:
                role = "admin"
                aggiuntoda = -1

        query = "INSERT INTO utente(id_utente, nome, cognome, data_nascita, email, password, ruolo, isFirstAccess, aggiunto_da, data_aggiunta, id_ristorante) VALUES (default, " + name + ", " + surname + ", " + datanascita + ", " + email + ", " + password + ", '" + role + "', " + isFirstAccess + ", " + str(aggiuntoda) + ", " + dataaggiunta + ", " + str(id_attivita) + ");\n"
        print(query)
        f.write(query)

        if role == "admin":
            numofadmin += 1

        if numofadmin < NUMBER_OF_ATTIVITA:
            NUMBER_OF_USER += 1

        u = User(idUser, name, surname, email, password, role, datanascita, isFirstAccess, aggiuntoda, dataaggiunta,
                 id_attivita)
        users.append(u)

        i += 1

        idUser += 1

    print(str(numofadmin) + " " + str(NUMBER_OF_ATTIVITA))


class User:
    id_utente = 0
    nome = ""
    cognome = ""
    data_nascita = ""
    email = ""
    password = ""
    ruolo = ""
    isFirstAccess = ""
    aggiuntoda = -1
    data_aggiunta = ""
    id_attivita = 0

    def __init__(self, id_utente, nome, cognome, email, password, ruolo, data_nascita, isFirstAccess,
                 aggiuntoda, data_aggiunta, id_attivita):
        self.id_utente = id_utente
        self.nome = nome
        self.cognome = cognome
        self.email = email
        self.password = password
        self.ruolo = ruolo
        self.data_nascita = data_nascita
        self.isFirstAccess = isFirstAccess
        self.aggiuntoda = aggiuntoda
        self.data_aggiunta = data_aggiunta
        self.id_attivita = id_attivita

    def getIdAttivita(self):
        return self.id_attivita

    def getRole(self):
        return self.ruolo

    def getIdUser(self):
        return self.id_utente


lstid = -1


def generateAttivita():
    global idUser, idAttivita, users, attivita, lstid
    for u in users:
        if u.getRole() == "admin":
            if lstid < u.getIdUser():
                apici = "'"
                response = requests.get("https://randomuser.me/api/")
                response2 = requests.get("https://random-word-api.herokuapp.com/word")
                js2 = response2.json()
                js = response.json()
                nome = apici + js2[0] + apici
                phone = apici + js['results'][0]['phone'] + apici
                indirizzo = apici + js['results'][0]['location']['city'] + " " + js['results'][0]['location'][
                    'state'] + " " + str(
                    random.randint(1, 100)) + apici
                img_name = apici + "img.png" + apici
                idproprietario = u.getIdUser()
                lstid = idproprietario
                query = "INSERT INTO ristorante (id_ristorante, nome, telefono, indirizzo, logo, nome_immagine, id_proprietario) VALUES (default, " + nome + ", " + phone + ", " + indirizzo + ", " + "NULL" + ", " + img_name + ", " + str(idproprietario) + ");\n"
                print(query)
                f.write(query)
                a = Attivita(idAttivita, nome, phone, indirizzo, " ", img_name, idproprietario)
                attivita.append(a)
                idAttivita += 1


class Attivita:
    id_attivita = 0
    nome = ""
    telefono = ""
    indirizzo = ""
    logo = ""
    img_name = ""
    id_proprietario = 0

    def __init__(self, id_attivita, nome, telefono, indirizzo, logo, img_name, id_proprietario):
        self.id_attivita = id_attivita
        self.nome = nome
        self.telefono = telefono
        self.indirizzo = indirizzo
        self.logo = logo
        self.img_name = img_name
        self.id_proprietario = id_proprietario

    def getIdAttivita(self):
        return self.id_attivita


def generateProdotto():
    global idProdotto
    apici = "'"
    searches = ["fruits", "vegetables-based-foods", "meats", "fishes", "eggs", "milks", "legumes",
                "cereals-and-their-product"]
    categoria_prodotti = [
        'frutta',
        'verdura',
        'carne',
        'pesce',
        'uova',
        'latte_e_derivati',
        'legumi',
        'cereali_e_derivati',
        'altro'
    ]
    kgOlt = ["kg", "lt"]
    i = 1
    j = 0
    x = 0
    numOfPages = 10
    word = searches[0]
    while i < numOfPages:
        try:
            if i == numOfPages - 1:
                x += 1
                word = searches[x]
                i = 1
            responseBevande = requests.get(
                "https://world.openfoodfacts.org/category/" + word + "?action=process&&json=true&&page=" + str(i))
            js = responseBevande.json()
            if i == 1:
                numOfPages = int(js["page_count"])
                numOfPages = numOfPages / 3
            for j in range(24):
                nome = js["products"][j]["product_name"]
                nome = nome.replace("'", "")
                descrizione = js["products"][j]["categories"]
                descrizione = descrizione.replace("'", "")
                costo = random.randint(1, 100) / random.randint(1, 3)
                costo = round(costo, 2)
                stato = random.randint(1, 100)
                quantita = random.randint(1, 100)
                unitamisura = "kg"
                id_ristorante = random.randint(1, NUMBER_OF_ATTIVITA)
                if word == searches[0]:
                    categoria = categoria_prodotti[0]
                elif word == searches[1]:
                    categoria = categoria_prodotti[1]
                elif word == searches[2]:
                    categoria = categoria_prodotti[2]
                elif word == searches[3]:
                    categoria = categoria_prodotti[3]
                elif word == searches[4]:
                    categoria = categoria_prodotti[4]
                elif word == searches[5]:
                    categoria = categoria_prodotti[5]
                    unitamisura = "lt"
                elif word == searches[6]:
                    categoria = categoria_prodotti[6]
                elif word == searches[7]:
                    categoria = categoria_prodotti[7]
                elif word == searches[8]:
                    categoria = categoria_prodotti[8]
                # print(str(idProdotto) + " " + nome + " descrizione " + str(costo) + " " + str(stato) + " " + str(quantita) + " " + unitamisura + " " + str(id_ristorante) + " " + categoria + " ")
                query = "INSERT INTO prodotto (id_prodotto, id_ristorante, nome, stato, descrizione, prezzo, quantita, unita_misura, categoria_prodotto) VALUES (default, " + str(id_ristorante) + ", " + apici + nome + apici + ", " + str(stato) + ", " + apici + descrizione + apici + ", " + str(costo) + ", " + str(quantita) + ", " + apici + unitamisura + apici + ", " + apici + categoria + apici + ");\n"
                print(query)
                f.write(query)
            i += 1
        except:
            i += 1
            print("")
    idProdotto += 1


class Prodotto:
    id_prodotto = 0
    nome = ""
    stato = 0
    descrizione = ""
    prezzo = 0
    quantita = 0
    unita_di_misura = ""  # kg o litri
    categoria = ""
    id_ristorante = 0

    def __init__(self, id_prodotto, nome, stato, descrizione, quantita, prezzo, unita_di_misura, categoria,
                 id_ristorante):
        self.id_prodotto = id_prodotto
        self.nome = nome
        self.stato = stato
        self.descrizione = descrizione
        self.quantita = quantita
        self.prezzo = prezzo
        self.categoria = categoria
        self.id_ristorante = id_ristorante
        self.unita_di_misura = unita_di_misura


def random_date(start, l):
    current = start
    while l >= 0:
        curr = current + datetime.timedelta(minutes=random.randrange(60))
        yield curr
        l -= 1


last2 = -1


def generateAvviso():
    global idAvviso, users, last2
    NUMBER_OF_AVVISI_FOR_EACH_ATTIVITA = 5
    ristorante = 0
    apici = "'"
    for i in range(NUMBER_OF_AVVISI):
        randomdate = ""
        l = [nouns, verbs, adj]
        if i % 2 == 0:
            startDate = datetime.datetime(2022, 9, 20, 13, 00)
        else:
            startDate = datetime.datetime(2022, 11, 20, 13, 00)
        for x in random_date(startDate, 10):
            randomdate = x.strftime("%Y-%m-%dT%H:%M")

        idscrittore = 0
        for u in users:
            if u.getRole() == "admin" or u.getRole() == "supervisore":
                if last2 < u.getIdUser():
                    last2 = u.getIdUser()
                    ristorante = u.getIdAttivita()
                    idscrittore = u.getIdUser()
                    testo = "AVVISO: " + ' '.join([random.choice(i) for i in l])
                    query = "INSERT INTO avviso(id_avviso, id_utente, id_ristorante, testo, data_ora) VALUES (default, " + str(idscrittore) + ", " + str(ristorante) + ", " + apici + testo + apici + ", " + apici + randomdate + apici + ");\n"
                    #print(str(idAvviso) + " " + testo + " " + randomdate + " " + str(ristorante) + " " + str(idscrittore))
                    print(query)
                    f.write(query)
                    a = Avviso(idAvviso, ristorante, idscrittore, "AVVISO: " + ' '.join([random.choice(i) for i in l]),
                               random_date)
                    avvisi.append(a)
                    idAvviso += 1


class Avviso:
    id_avviso = 0
    id_ristorante = 0
    id_utente = 0
    testo = ""
    data_ora = ""

    def __init__(self, id_avviso, id_ristorante, id_utente, testo, data_ora):
        self.id_avviso = id_avviso
        self.id_ristorante = id_ristorante
        self.id_utente = id_utente
        self.testo = testo
        self.data_ora = data_ora

    def getIdAttivita(self):
        return self.id_ristorante

    def getIdAvviso(self):
        return self.id_avviso


# vale sia per nascosti che per visti
def generaCronologiaMessaggi(vistiOrNascosti):
    global attivita, users, avvisi, NUM_MAX_OF_AVVISI_NASCOSTI
    apici = "'"
    i = 0
    for u in users:
        i = 0
        num_of_avvisi_nascosti = random.randint(0, NUM_MAX_OF_AVVISI_NASCOSTI)
        for a in avvisi:
            if num_of_avvisi_nascosti == 0:
                break
            if a.getIdAttivita() == u.getIdAttivita() and i < num_of_avvisi_nascosti:
                if vistiOrNascosti == 1:
                    query = "INSERT INTO cronologia_lettura_avviso (id_utente, id_avviso, data_lettura) VALUES (" +  str(u.getIdUser()) + ", " + str(a.getIdAvviso()) +", '2022-09-20T13:54'"+");\n"
                else:
                    query = "INSERT INTO cronologia_nascosti_avviso (id_utente, id_avviso, data_nascosto) VALUES (" +  str(u.getIdUser()) + ", " + str(a.getIdAvviso()) +", '2022-09-20T13:54'" +");\n"
                # print("id_user: " + str(u.getIdUser()) + " id_avviso: " + str(a.getIdAvviso()) + " (di debug) id_ristorante: " + str(a.getIdAttivita()))
                print(query)
                f.write(query)
                i += 1


class Cronologia:
    id_utente = 0
    id_avvisi = 0

    def __init__(self, id_avviso, id_utente):
        self.id_avviso = id_avviso
        self.id_utente = id_utente


def generateCategoria():
    global attivita, categorieMenu
    categorie = ["primi", "secondi", "dessert", "bevande"]
    i = 1
    apici = "'"
    for a in attivita:
        for c in categorie:
            #print(str(i), a.getIdAttivita(), categorie[i % 4])
            query = "INSERT INTO categorie_menu(id_categoria, id_ristorante, nome, posizione_categoria) VALUES (default, " + str(a.getIdAttivita()) + ", " + apici + categorie[i % 4] + apici + ", NULL);\n"
            print(query)
            f.write(query)
            c = Categoria(i, a.getIdAttivita(), categorie[i % 4])
            categorieMenu.append(c)
            i += 1


class Categoria:
    id_categoria = 0
    id_ristorante = 0
    nome = ""

    def __init__(self, id_categoria, id_ristorante, nome):
        self.nome = nome
        self.id_ristorante = id_ristorante
        self.id_categoria = id_categoria

    def getIdCategoria(self):
        return self.id_categoria

    def getIdRistorante(self):
        return self.id_ristorante

    def getNome(self):
        return self.nome


def fromIdCategoriaToIdRistorante(idcategoria):
    global categorieMenu
    while True:
        index = random.randint(0, categorieMenu.__len__() - 1)
        if categorieMenu[index].getIdCategoria() == idcategoria:
            return categorieMenu[index].getIdRistorante()


def getRandomIdOfCategoryOfPiatto(categoria):
    global categorieMenu
    while True:
        index = random.randint(0, categorieMenu.__len__() - 1)
        if categorieMenu[index].getNome() == categoria:
            return categorieMenu[index].getIdCategoria()


def generatePiatto2():
    NUM_OF_CATEGORIE = 4
    # mancano secondi
    apici = "'"
    searches = ["beverages", "pastas", "dessert", "pizzas", "sandwiches", "mexican"]
    # response = requests.get("https://it.openfoodfacts.org/cgi/search.pl?action=process&&tagtype_1=nutrition_grades&&tag_contains_1=contains&&tag_1=A&&json=true&&page=1")
    i = 1
    j = 0
    x = 0
    numOfPages = 10
    word = searches[0]
    while i < numOfPages:
        try:
            if i == numOfPages - 1:
                x += 1
                word = searches[x]
                i = 1
            responseBevande = requests.get(
                "https://world.openfoodfacts.org/category/" + word + "?action=process&&json=true&&page=" + str(i))
            js = responseBevande.json()
            if i == 1:
                numOfPages = int(js["page_count"])
                if word != "beverages" and word != "pastas" and word != "dessert":
                    numOfPages = numOfPages / 3
                else:
                    numOfPages = numOfPages / 2

            for j in range(24):
                nome = js["products"][j]["product_name"]
                nome = nome.replace("'", "")
                descrizione = js["products"][j]["categories"]
                descrizione = descrizione.replace("'", "")
                costo = random.randint(1, 100) / random.randint(1, 3)
                costo = round(costo, 2)
                allergeni = js["products"][j]["allergens"]

                if word == searches[0]:
                    id_categoria = getRandomIdOfCategoryOfPiatto("bevande")
                elif word == searches[1]:
                    id_categoria = getRandomIdOfCategoryOfPiatto("primi")
                elif word == searches[2]:
                    id_categoria = getRandomIdOfCategoryOfPiatto("dessert")
                else:
                    id_categoria = getRandomIdOfCategoryOfPiatto("secondi")

                #print(nome + " descrizione " + str(costo) + " " + allergeni + " " + str(id_categoria) + " " + str(fromIdCategoriaToIdRistorante(id_categoria)))
                query = "INSERT INTO elementi_menu(id_elemento, id_ristorante, id_categoria, nome, prezzo, descrizione, allergeni, nome_seconda_lingua, descrizione_seconda_lingua, posizione_elemento) VALUES (default, " + str(fromIdCategoriaToIdRistorante(id_categoria)) + ", " + str(id_categoria) + ", " + apici + nome + apici + ", " + str(costo) + ", " + apici + descrizione + apici + ", " + apici +  allergeni + apici + ", NULL, NULL, NULL);\n"
                print(query)
                f.write(query)
            i += 1
        except:
            i += 1
            print("")



class ElementiMenu:
    id_piatto = 0
    id_ristorante = 0
    id_categoria = 0
    nome = ""
    costo = 0
    descrizione = ""
    allergeni = ""
    name = ""
    description = ""

    def __init__(self, id_piatto, nome, descrizione, costo, allergeni, id_categoria, name, description, id_ristorante):
        self.id_piatto = id_piatto
        self.id_ristorante = id_ristorante
        self.nome = nome
        self.descrizione = descrizione
        self.costo = costo
        self.allergeni = allergeni
        self.id_categoria = id_categoria
        self.name = name
        self.description = description


def generateMenu():
    id_ristorante = 0
    id_menu = 0
    id_categoria = 0
    id_piatto = 0
    for i in range(NUMBER_OF_MENU):
        print(id_ristorante + " " + id_menu + " " + id_categoria + " " + id_piatto)


class Menu:
    id_ristorante = 0
    id_menu = 0
    id_categoria = 0
    id_piatto = 0

    def __int__(self, id_menu, id_categoria, id_piatto):
        self.id_menu = id_menu
        self.id_categoria = id_categoria
        self.id_piatto = id_piatto


def main():
    f.write("ALTER SEQUENCE utente_id_utente_seq RESTART WITH 1;")
    generateUser()
    f.write("ALTER SEQUENCE ristorante_id_ristorante_seq RESTART WITH 1;")
    generateAttivita()
    f.write("ALTER SEQUENCE avviso_id_avviso_seq RESTART WITH 1;")
    generateAvviso()
    generaCronologiaMessaggi(1)
    generaCronologiaMessaggi(2)
    f.write("ALTER SEQUENCE categorie_menu_id_categoria_seq RESTART WITH 1;")
    generateCategoria()
    f.write("ALTER SEQUENCE elementi_menu_id_elemento_seq RESTART WITH 1;")
    generatePiatto2()
    f.write("ALTER SEQUENCE elementi_menu_id_elemento_seq RESTART WITH 1;")
    generateProdotto()
    f.close()


if __name__ == "__main__":
    main()
