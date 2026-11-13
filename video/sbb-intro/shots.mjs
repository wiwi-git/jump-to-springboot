// Screenshots of the running SBB app (port 8091, seeded demo data) → assets/shots/*.png
// usage: node shots.mjs <path to puppeteer-core>   (hyperframes' npx cache has one)
const puppeteer = (await import(process.argv[2])).default;
const B = "http://localhost:8091";
const browser = await puppeteer.launch({
  executablePath: "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
  defaultViewport: { width: 1280, height: 860, deviceScaleFactor: 1.5 },
});
const page = await browser.newPage();
const shot = async (url, name) => {
  await page.goto(B + url, { waitUntil: "networkidle0" });
  await page.screenshot({ path: `assets/shots/${name}.png` });
};
await shot("/user/signup", "signup");
await page.goto(B + "/user/login", { waitUntil: "networkidle0" });
await page.type("#username", "user1");
await page.type("#password", "1234");
await Promise.all([page.waitForNavigation(), page.click("button[type=submit]")]);
await shot("/question/list", "list");
await shot("/question/list?kw=LAZY", "search");
await shot(`/question/detail/${process.argv[3]}`, "detail");
await browser.close();
