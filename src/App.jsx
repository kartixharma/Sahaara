import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./components/home/home";
import { Connect } from "./components/connect/Connect";
import { LoginPage } from "./components/LoginPage/LoginPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/connect" element={<Connect />} />
      </Routes>
    </Router>
  );
}

export default App;
