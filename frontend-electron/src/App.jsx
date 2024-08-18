import { useRef, useState } from "react";
import axios from "axios";
import "./App.css";
import fs from "fs";

function App() {
  const [plastic, setPlastic] = useState(0);
  const [metal, setMetal] = useState(0);
  const [glass, setGlass] = useState(0);

  const plasticRef = useRef(null);
  const metalRef = useRef(null);
  const glassRef = useRef(null);

  function handlePrint() {
    // Create a document

    let data = JSON.stringify({
      glass: glass,
      metal: metal,
      plastic: plastic,
    });
    let config = {
      method: "post",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/api/register",
      headers: {
        "Content-Type": "application/json",
      },
      data: data,
    };
    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        let Receipt = response.data;
        let params = {
          id: Receipt.id,
          total: 0.5 * (Number(glass) + Number(plastic) + Number(metal)),
          data: new Date(Date.parse(Receipt.localDateTime)),
          metal: Receipt.totalMetal,
          plastic: Receipt.totalPlastic,
          glass: Receipt.totalGlass,
        };
        console.log(new Date(Date.parse(params.data)));
        window.electronAPI.savePDF(params);
      })
      .catch((error) => {
        console.log(error);
      });
    handleReset();
  }

  function handleReset() {
    setPlastic(0);
    setGlass(0);
    setMetal(0);
  }

  function handleKeyDown(e, nextRef) {
    if (e.key === "Enter") {
      e.preventDefault();
      nextRef.current.focus();
    }
  }

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 className="mt-5 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Gestiune Returo
        </h2>
      </div>

      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6">
          <div>
            <div className="flex items-center justify-between">
              <label
                htmlFor="metal"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Metal
              </label>
            </div>
            <div className="mt-2">
              <input
                id="metal"
                name="metal"
                value={metal}
                ref={metalRef}
                type="number"
                onChange={(e) => setMetal(Number(e.target.value))}
                onFocus={() => setMetal("")}
                onKeyDown={(e) => handleKeyDown(e, plasticRef)}
                required
                min={0}
                className="pl-1 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>

          <div>
            <div className="flex items-center justify-between">
              <label
                htmlFor="plastic"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Plastic
              </label>
            </div>
            <div className="mt-2">
              <input
                id="plastic"
                name="plastic"
                value={plastic}
                ref={plasticRef}
                type="number"
                onChange={(e) => setPlastic(Number(e.target.value))}
                onFocus={() => setPlastic("")}
                onKeyDown={(e) => handleKeyDown(e, glassRef)}
                required
                min={0}
                className="pl-1 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>

          <div>
            <div className="flex items-center justify-between">
              <label
                htmlFor="glass"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Sticla
              </label>
            </div>
            <div className="mt-2">
              <input
                id="glass"
                name="glass"
                type="number"
                value={glass}
                ref={glassRef}
                onChange={(e) => setGlass(Number(e.target.value))}
                onFocus={() => setGlass("")}
                onKeyDown={(e) => handleKeyDown(e, metalRef)}
                required
                min={0}
                className="pl-1 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>

          <h3 style={{ color: "red", textAlign: "center", fontSize: "24px" }}>
            <strong>
              Total: {0.5 * (Number(plastic) + Number(glass) + Number(metal))}
            </strong>
          </h3>

          {metal > 0 || glass > 0 || plastic > 0 ? (
            <div>
              <button
                type="button"
                onClick={handlePrint}
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Printeaza
              </button>
            </div>
          ) : (
            ""
          )}
          {plastic > 0 || metal > 0 || glass > 0 ? (
            <div>
              <button
                type="button"
                onClick={handleReset}
                className="flex w-full justify-center rounded-md bg-black px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Reseteaza
              </button>
            </div>
          ) : (
            ""
          )}
        </form>
      </div>
    </div>
  );
}

export default App;
