import "./App.css";
import axios from "axios";
import { useState } from "react";

export default function App() {
  const [dateInput, setDateInput] = useState("");
  const [resp, setResp] = useState({
    metal: "",
    plastic: "",
    glass: "",
    total: "",
    data: null,
  });

  function handleTotalCreation() {
    let config = {
      method: "get",
      maxBodyLength: Infinity,
      url: `http://localhost:8050/api/getTotalReceiptByDate?date=${dateInput}`,
      headers: {},
    };

    axios
      .request(config)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        let totals = response.data;
        setResp({
          metal: totals.totalMetal,
          plastic: totals.totalPlastic,
          glass: totals.totalGlass,
          total: totals.totalPrice,
          data: new Date(Date.parse(totals.localDateTime)),
        });
        console.log(resp);
        console.log(new Date(Date.parse(resp.data)));
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function handlePdfCreationAndPrint() {
    let data = JSON.stringify({
      date: dateInput,
    });

    let config = {
      method: "post",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/api/generateAndPrintDailyTotalPdf",
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
  }

  function handlePdfCreationAndPrintVoucher() {
    let data = JSON.stringify({
      date: dateInput,
    });

    let config = {
      method: "post",
      maxBodyLength: Infinity,
      url: "http://localhost:8050/api/generateAndPrintDailyVoucherTotalPdf",
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
  }

  function formatDate(date) {
    if (!(date instanceof Date) || isNaN(date.getTime())) return "";
    const day = date.getDate().toString();
    const month = (date.getMonth() + 1).toString();
    const year = date.getFullYear().toString();
    return `${day}-${month}-${year}`;
  }

  return (
    <div className="containter">
      <button className="button" onClick={handlePdfCreationAndPrint}>
        Creeaza si printeaza totalul pentru plata numerar
      </button>
      <button className="button" onClick={handlePdfCreationAndPrintVoucher}>
        Creeaza si printeaza totalul pentru vouchere
      </button>
      <button className="button" onClick={handleTotalCreation}>
        Vizualizeaza total
      </button>
      <input
        type="date"
        onChange={(e) => setDateInput(e.target.value)}
        value={dateInput}
        className="input"
      ></input>
      {dateInput && (
        <table className="styled-table">
          <thead>
            <tr>
              <th>Total metal</th>
              <th>Total plastic</th>
              <th>Total sticla</th>
              <th>Total</th>
              <th>Data</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{resp.metal}</td>
              <td>{resp.plastic}</td>
              <td>{resp.glass}</td>
              <td>{resp.total}</td>
              <td>{formatDate(resp.data)}</td>
            </tr>
          </tbody>
        </table>
      )}
    </div>
  );
}
