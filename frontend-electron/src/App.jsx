import { useRef, useState, useEffect } from "react";
import axios from "axios";
import "./App.css";
import Input from "./Input.jsx";
import Button from "./Button.jsx";

export default function App() {
  const [plastic, setPlastic] = useState(0);
  const [metal, setMetal] = useState(0);
  const [glass, setGlass] = useState(0);
  const [uuid, setUuid] = useState("");
  const [message, setMessage] = useState("");
  const [isVisible, setIsVisible] = useState(false);

  const plasticRef = useRef(null);
  const metalRef = useRef(null);
  const glassRef = useRef(null);
  const barcodeRef = useRef(null);

  function handlePrint() {
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
      })
      .catch((error) => {
        console.log(error);
      });
    handleReset();
  }

  function handleVoucher() {
    let data = JSON.stringify({
      plastic: plastic,
      metal: metal,
      glass: glass,
    });

    let config = {
      method: "post",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/barcodes/qr",
      headers: {
        "Content-Type": "application/json",
      },
      data: data,
    };
    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
      })
      .catch((error) => {
        console.log(error);
      });
    handleReset();
  }

  function handleScan() {
    let data = JSON.stringify({
      uuid: uuid,
    });

    let config = {
      method: "post",
      maxBodyLength: Infinity,
      url: `http://localhost:8050/barcodes/redeem?uuid=${uuid}`,
      headers: {
        "Content-Type": "application/json",
      },
      data: data,
    };

    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        setMessage(response.data);
        setIsVisible(true);
        setTimeout(() => {
          setIsVisible(false);
        }, 10000);
        // console.log(message);
      })
      .catch((error) => {
        console.log(error.response.data);
        setMessage(error.response.data);
        setIsVisible(true);
        setTimeout(() => {
          setIsVisible(false);
        }, 10000);
        // console.log(message);
      });
    handleReset();
  }

  function handleReset() {
    setPlastic(0);
    setGlass(0);
    setMetal(0);
    barcodeRef.current.focus(); // Focus back to barcode input
  }

  function handleKeyDown(e, nextRef) {
    if (e.key === "Enter") {
      e.preventDefault();
      nextRef.current.focus();
    }
  }

  useEffect(() => {
    // Set focus on barcode input when the component mounts
    barcodeRef.current.focus();
  }, []);

  useEffect(() => {
    // Add event listener for clicks outside input fields and buttons
    const handleClickOutside = (event) => {
      if (
        barcodeRef.current &&
        !barcodeRef.current.contains(event.target) &&
        !metalRef.current.contains(event.target) &&
        !plasticRef.current.contains(event.target) &&
        !glassRef.current.contains(event.target) &&
        !event.target.closest("button")
      ) {
        barcodeRef.current.focus();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  useEffect(() => {
    // Handle scan input changes
    if (uuid) {
      handleScan(uuid);
    }
  }, [uuid]);

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 className="mt-5 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Gestiune Returo
        </h2>
      </div>
      <div>
        <input
          id="barcode"
          name="barcode"
          type="text"
          ref={barcodeRef}
          value={uuid}
          onChange={(e) => setUuid(e.target.value)}
          onFocus={() => setUuid("")}
          onBlur={(e) => {
            if (!e.relatedTarget) {
              e.target.focus();
            }
          }}
        />
        {isVisible && <h1 className="alert-message">{message}</h1>}
      </div>

      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6">
          <Input
            label="Metal"
            name="Metal"
            value={metal}
            inputRef={metalRef}
            onChange={(e) => setMetal(Number(e.target.value))}
            onFocus={() => setMetal("")}
            onKeyDown={(e) => handleKeyDown(e, plasticRef)}
          />

          <Input
            label="Plastic"
            name="Plastic"
            value={plastic}
            inputRef={plasticRef}
            onChange={(e) => setPlastic(Number(e.target.value))}
            onFocus={() => setPlastic("")}
            onKeyDown={(e) => handleKeyDown(e, glassRef)}
          />

          <Input
            label="Glass"
            name="Sticla"
            value={glass}
            inputRef={glassRef}
            onChange={(e) => setGlass(Number(e.target.value))}
            onFocus={() => setGlass("")}
            onKeyDown={(e) => handleKeyDown(e, metalRef)}
          />

          {metal > 0 || glass > 0 || plastic > 0 ? (
            <>
              <h3
                style={{ color: "red", textAlign: "center", fontSize: "24px" }}
              >
                <strong>
                  Total:{" "}
                  {0.5 * (Number(plastic) + Number(glass) + Number(metal))}
                </strong>
              </h3>

              <Button onClick={handleVoucher}>Voucher</Button>
              <Button onClick={handlePrint}>Plata numerar</Button>
              <Button onClick={handleReset}>Reseteaza</Button>
            </>
          ) : (
            ""
          )}
        </form>
      </div>
    </div>
  );
}
